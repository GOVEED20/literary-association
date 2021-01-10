package goveed20.PaypalPaymentService.controllers;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginRetailerController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
import goveed20.PaypalPaymentService.services.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RetailerController implements PluginRetailerController {

    @Autowired
    private RetailerService retailerService;

    @Override
    public ResponseEntity<ServiceFieldsCheck> checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        try {
            return new ResponseEntity<>(retailerService.checkPaymentServiceFields(payload), HttpStatus.OK);
        } catch (BadRequestException e) {
            ServiceFieldsCheck serviceFieldsCheck = ServiceFieldsCheck.builder()
                    .validationMessage(e.getMessage())
                    .build();
            return new ResponseEntity<>(serviceFieldsCheck, HttpStatus.BAD_REQUEST);
        }
    }
}
