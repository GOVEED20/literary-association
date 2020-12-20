package goveed20.PaypalPaymentService.controllers;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionDataPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import goveed20.PaypalPaymentService.services.PaymentService;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class PaymentController implements PluginController {
    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<?> initializePayment(@Valid InitializationPaymentPayload payload) {
        try {
            paymentService.initializePayment(payload);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Request failed", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> completePayment(@Valid TransactionDataPayload payload) {
        try {
            paymentService.completePayment(payload);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Request failed", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields() {
        return null;
    }
}
