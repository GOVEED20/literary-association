package goveed20.PaypalPaymentService.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PaymentConcentratorFeignClient;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
import goveed20.PaypalPaymentService.model.PaypalPaymentIntent;
import goveed20.PaypalPaymentService.model.PaypalPaymentMethod;
import goveed20.PaypalPaymentService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private APIContext apiContext;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    public String initializePayment(InitializationPaymentPayload payload) throws PayPalRESTException, UnknownHostException {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(payload.getAmount().toString());

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("payee")) {
            throw new BadRequestException("Missing payee email");
        }

        Payee payee = new Payee();
        payee.setEmail(payload.getPaymentFields().get("payee"));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setPayee(payee);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.toString());

        Payment payment = new Payment();
        payment.setIntent(PaypalPaymentIntent.sale.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(baseUrl + "/api/complete-payment");
        redirectUrls.setCancelUrl(baseUrl + "/api/complete-payment");

        payment.setRedirectUrls(redirectUrls);

        Payment initializedPayment = payment.create(apiContext);

        goveed20.PaypalPaymentService.model.Transaction internalTransaction = goveed20.PaypalPaymentService.model.Transaction.builder()
                .transactionId(payload.getTransactionId())
                .payment(initializedPayment)
                .build();

        transactionRepository.save(internalTransaction);

        return initializedPayment.getLinks()
                .stream()
                .filter(l -> l.getRel().equals("approval_url"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Bad paypal link"))
                .getHref();
    }

    public void completePayment(Map<String, String[]> paramMap) {
        String paymentId = Arrays.stream(paramMap.get("paymentId"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Payment id not present"));

        String payerId = Arrays.stream(paramMap.get("PayerID"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Payer id not present"));

        goveed20.PaypalPaymentService.model.Transaction internalTransaction = transactionRepository
                .findTransactionByPayment(paymentId)
                .orElseThrow(() -> new BadRequestException("Transaction id invalid")); // fix query in compas

        Payment payment = internalTransaction.getPayment();

        if (payment.getState().equals("failed")) {
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(internalTransaction.getTransactionId())
                            .transactionStatus(TransactionStatus.FAILED)
                            .build()
            );
        }

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            payment.execute(apiContext, paymentExecution);

            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(internalTransaction.getTransactionId())
                            .transactionStatus(TransactionStatus.SUCCESS)
                            .build()
            );

        } catch (PayPalRESTException e) {
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(internalTransaction.getTransactionId())
                            .transactionStatus(TransactionStatus.ERROR)
                            .build()
            );
        }
    }
}
