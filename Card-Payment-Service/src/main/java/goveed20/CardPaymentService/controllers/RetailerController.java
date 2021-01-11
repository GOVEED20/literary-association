package goveed20.CardPaymentService.controllers;

import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.services.RetailerService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginRetailerController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
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
        return new ResponseEntity<>(retailerService.checkPaymentServiceFields(payload), HttpStatus.OK);
    }
}
