package goveed20.PaypalPaymentService.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
import goveed20.PaypalPaymentService.model.PaypalPaymentIntent;
import goveed20.PaypalPaymentService.model.PaypalPaymentMethod;
import goveed20.PaypalPaymentService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class PaymentService {
    @Autowired
    private APIContext apiContext;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private Environment environment;

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

        String baseUrl = String.format("%s:%d", InetAddress.getLocalHost().getHostAddress(),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.port"))));


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
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
        }
    }
}
