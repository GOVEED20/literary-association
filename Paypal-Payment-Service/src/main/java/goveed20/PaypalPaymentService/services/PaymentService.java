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
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.UnknownHostException;
import java.util.*;

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
                .findFirst().orElse(null);

        String payerId = Arrays.stream(paramMap.get("PayerID"))
                .findFirst().orElse(null);

        goveed20.PaypalPaymentService.model.Transaction internalTransaction = transactionRepository
                .findTransactionByPayment(paymentId).orElse(null);

        assert internalTransaction != null;
        Payment payment = internalTransaction.getPayment();

        if (payerId == null || paymentId == null) {
            sendTransactionResponse(internalTransaction.getTransactionId(), TransactionStatus.FAILED);
        }

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            payment.execute(apiContext, paymentExecution);
            sendTransactionResponse(internalTransaction.getTransactionId(), TransactionStatus.SUCCESS);
        } catch (PayPalRESTException e) {
            sendTransactionResponse(internalTransaction.getTransactionId(), TransactionStatus.ERROR);
        }
    }

    @Async
    @SneakyThrows
    public void sendTransactionResponse(UUID transactionId, TransactionStatus status) {
        paymentConcentratorFeignClient.sendTransactionResponse(
                ResponsePayload.childBuilder()
                        .transactionID(transactionId)
                        .transactionStatus(status)
                        .build()
        );
    }
}
