package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.InitializePaymentRequest;
import goveed20.PaymentConcentrator.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.exceptions.NotFoundException;
import goveed20.PaymentConcentrator.exceptions.StatusCodeException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/payment-services")
    public ResponseEntity<Set<String>> getGlobalPaymentServices() {
        return new ResponseEntity<>(paymentService.getGlobalPaymentServices(), HttpStatus.OK);
    }

    @GetMapping(value = "{retailerName}/payment-services")
    public ResponseEntity<?> getRetailerPaymentServices(@PathVariable("retailerName") String retailerName) {
        try {
            return new ResponseEntity<>(paymentService.getRetailerPaymentServices(retailerName), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/payment-services/{paymentService}/registration-fields")
    public ResponseEntity<?> getPaymentServiceRegistrationFields(@PathVariable("paymentService") String paymentServiceName) {
        try {
            return new ResponseEntity<>(paymentService.getPaymentServiceRegistrationFields(paymentServiceName), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusCodeException e) {
            return new ResponseEntity<>("Request failed", e.getStatusCode());
        }
    }

    @PostMapping(value = "/payment-services/{paymentService}/initialize-payment")
    public ResponseEntity<String> initializePayment(@PathVariable("paymentService") String paymentServiceName,
                                                     @Valid @RequestBody InitializePaymentRequest paymentRequest) {
        try {
            return new ResponseEntity<>(paymentService.initializePayment(paymentServiceName, paymentRequest), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusCodeException e) {
            return new ResponseEntity<>("Request failed", e.getStatusCode());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> sendTransactionResponse(@RequestBody ResponsePayload responsePayload) {
        try {
            paymentService.sendTransactionResponse(responsePayload);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusCodeException e) {
            return new ResponseEntity<>("Request failed", e.getStatusCode());
        }
    }

}
