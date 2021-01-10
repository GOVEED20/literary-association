package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.RetailerData;
import goveed20.PaymentConcentrator.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.services.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RetailerController {

    @Autowired
    private RetailerService retailerService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> registerRetailer(@RequestPart("retailerData") RetailerData retailerData) {
        try {
            return new ResponseEntity<>(retailerService.registerRetailer(retailerData), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
