package goveed20.CardPaymentService.controllers;

import goveed20.CardPaymentService.services.PaymentService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class PaymentController implements PluginController {

    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<String> initializePayment(@Valid InitializationPaymentPayload payload) {
        try {
            return new ResponseEntity<>(paymentService.initializePayment(payload), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> completePayment(HttpServletRequest request) {
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long transactionId = Long.parseLong(String.valueOf(pathVariables.get("transactionId")));

        paymentService.completePayment(transactionId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields() {
        return null;
    }
}
