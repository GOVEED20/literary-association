package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.InitializePaymentRequest;
import goveed20.PaymentConcentrator.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.exceptions.NotFoundException;
import goveed20.PaymentConcentrator.exceptions.StatusCodeException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/payment-services")
    private ResponseEntity<Set<String>> getGlobalPaymentServices() {
        return new ResponseEntity<>(paymentService.getGlobalPaymentServices(), HttpStatus.OK);
    }

    @GetMapping(value = "{retailerId}/payment-services")
    private ResponseEntity<?> getRetailerPaymentServices(@PathVariable("retailerId") Long retailerId) {
        try {
            return new ResponseEntity<>(paymentService.getRetailerPaymentServices(retailerId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/payment-services/{paymentService}/registration-fields")
    private ResponseEntity<?> getPaymentServiceRegistrationFields(@PathVariable("paymentService") String paymentServiceName) {
        try {
            return new ResponseEntity<>(paymentService.getPaymentServiceRegistrationFields(paymentServiceName), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusCodeException e) {
            return new ResponseEntity<>("Request failed", e.getStatusCode());
        }
    }

    @PostMapping(value = "/payment-services/{paymentService}/initialize-payment")
    private ResponseEntity<?> initializePayment(@PathVariable("paymentService") String paymentServiceName,
                                                @Valid @RequestBody InitializePaymentRequest paymentRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", paymentService.initializePayment(paymentServiceName, paymentRequest));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusCodeException e) {
            return new ResponseEntity<>("Request failed", e.getStatusCode());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/payment")
    ResponseEntity<?> sendTransactionResponse(@RequestBody ResponsePayload responsePayload) {
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
