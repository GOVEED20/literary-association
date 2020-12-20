package goveed20.PaypalPaymentService.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PaymentConcentratorFeignClient;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionDataPayload;
import goveed20.PaypalPaymentService.model.PaypalPaymentIntent;
import goveed20.PaypalPaymentService.model.PaypalPaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import goveed20.PaypalPaymentService.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private APIContext apiContext;

    @Autowired
    private TransactionRepository transactionRepository;

    public void initializePayment(InitializationPaymentPayload payload) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(payload.getAmount().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.toString());

        Payment payment = new Payment();
        payment.setIntent(PaypalPaymentIntent.sale.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(payload.getSuccessURL());
        redirectUrls.setCancelUrl(payload.getFailedURL());

        payment.setRedirectUrls(redirectUrls);

        Payment initializedPayment = payment.create(apiContext);

        goveed20.PaypalPaymentService.model.Transaction internalTransaction = goveed20.PaypalPaymentService.model.Transaction.builder()
                .transactionId(payload.getTransactionId())
                .payment(initializedPayment)
                .build();

        transactionRepository.save(internalTransaction);
    }

    public void completePayment(TransactionDataPayload transactionDataPayload) {

    }
}
