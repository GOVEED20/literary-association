package goveed20.BitcoinPaymentService.controllers;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionDataPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
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
