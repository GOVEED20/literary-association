package goveed20.BitcoinPaymentService.controllers;

import goveed20.BitcoinPaymentService.dtos.CompleteBitcoinOrder;
import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.services.PaymentService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class PaymentController implements PluginController {

    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseEntity<String> initializePayment(@Valid InitializationPaymentPayload payload) {
        try {
            log.info("BTC PaymentController: Initialize transaction with id " + payload.getTransactionId()
                    + " and amount " + payload.getAmount());

            return new ResponseEntity<>(paymentService.initializePayment(payload), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestBody CompleteBitcoinOrder order) {
        System.out.println("Complete BTC");
        return null;
    }

    @Override
    public ResponseEntity<?> completePaymentPost(HttpServletRequest request) {
        try {
            log.info("BTC PaymentController: Started completing transaction");
            paymentService.completePayment(request.getReader().lines().collect(Collectors.joining()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> completePaymentGet(HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields() {
        return null;
    }
}
