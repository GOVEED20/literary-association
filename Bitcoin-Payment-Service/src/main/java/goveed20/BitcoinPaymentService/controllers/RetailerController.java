package goveed20.BitcoinPaymentService.controllers;

import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.services.RetailerService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginRetailerController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
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
    public ResponseEntity<String> checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        try {
            return new ResponseEntity<>(retailerService.checkPaymentServiceFields(payload), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
