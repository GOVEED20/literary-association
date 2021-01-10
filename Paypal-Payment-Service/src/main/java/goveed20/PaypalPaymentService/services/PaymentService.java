package goveed20.PaypalPaymentService.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.*;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
import goveed20.PaypalPaymentService.model.PaypalPaymentIntent;
import goveed20.PaypalPaymentService.model.PaypalPaymentMethod;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class PaymentService {
    @Autowired
    private APIContext apiContext;

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    @Autowired
    private PaypalSubscriptionsService paypalSubscriptionsService;

    public Set<RegistrationField> getPaymentServiceRegistrationFields() {
        Map<String, String> validationConstraints = new HashMap<>();
        validationConstraints.put("type", "email");
        validationConstraints.put("required", "required");

        RegistrationField payee = RegistrationField.builder()
                .encrypted(false)
                .name("payee")
                .type(RegistrationFieldType.STRING)
                .validationConstraints(validationConstraints)
                .build();

        Set<RegistrationField> registrationFields = new HashSet<>();
        registrationFields.add(payee);

        return registrationFields;
    }

    public String initializePayment(InitializationPaymentPayload payload) throws PayPalRESTException {
        if (payload.getPaymentFields().containsKey("subscription")) {
            return "."; // integrate subscriptions
        } else {
            Payment payment = createPayment(payload);

            return payment.getLinks()
                    .stream()
                    .filter(l -> l.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Bad paypal link"))
                    .getHref();
        }
    }

    public void completePayment(Long transactionId, Map<String, String[]> paramMap) {
        String[] paymentId = paramMap.getOrDefault("paymentId", null);
        String[] payerId = paramMap.getOrDefault("PayerID", null);

        if (payerId == null || paymentId == null) {
            sendTransactionResponse(transactionId, TransactionStatus.FAILED);
            return;
        }

        Payment payment = new Payment();
        payment.setId(paymentId[0]);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId[0]);

        try {
            payment.execute(apiContext, paymentExecution);
            sendTransactionResponse(transactionId, TransactionStatus.SUCCESS);
        } catch (PayPalRESTException e) {
            sendTransactionResponse(transactionId, TransactionStatus.ERROR);
        }
    }

    private Payment createPayment(InitializationPaymentPayload payload) throws PayPalRESTException {
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

        RedirectUrls redirectUrls = new RedirectUrls();

        String callbackUrl = buildCallbackUrl(payload.getTransactionId());

        redirectUrls.setReturnUrl(callbackUrl);
        redirectUrls.setCancelUrl(callbackUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @SneakyThrows
    public static String buildCallbackUrl(Long transactionId) {
        UriComponents context = ServletUriComponentsBuilder.fromCurrentContextPath().build();

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(context.getHost())
                .port(context.getPort())
                .path(String.format("/api/complete-payment/%d", transactionId))
                .build();

        return uriComponents.toUri().toURL().toString();
    }

    @Async
    @SneakyThrows
    public void sendTransactionResponse(Long transactionId, TransactionStatus status) {
        paymentConcentratorFeignClient.sendTransactionResponse(
                ResponsePayload.builder()
                        .transactionID(transactionId)
                        .transactionStatus(status)
                        .build()
        );
    }
}
