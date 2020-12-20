package controllers;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionDataPayload;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Set;

public class PaymentController implements PluginController {
    @Override
    public ResponseEntity<?> initializePayment(@Valid InitializationPaymentPayload payload) {
        return null;
    }

    @Override
    public ResponseEntity<?> completePayment(@Valid TransactionDataPayload payload) {
        return null;
    }

    @Override
    public ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields() {
        return null;
    }
}
