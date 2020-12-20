package goveed20.PaypalPaymentService.controllers;

import com.paypal.base.rest.PayPalRESTException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
import goveed20.PaypalPaymentService.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.UnknownHostException;
import java.util.Set;

@RestController
public class PaymentController implements PluginController {
    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<String> initializePayment(@Valid @RequestBody InitializationPaymentPayload payload) {
        try {
            return new ResponseEntity<>(paymentService.initializePayment(payload), HttpStatus.NO_CONTENT);
        } catch (PayPalRESTException | UnknownHostException e) {
            return new ResponseEntity<>("Request failed", HttpStatus.BAD_REQUEST);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> completePaymentGet(HttpServletRequest request) {
        try {
            paymentService.completePayment(request.getParameterMap());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Request failed", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> completePaymentPost(HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields() {
        return null;
    }
}
