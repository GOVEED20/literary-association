package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.services.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retailer")
@CrossOrigin
public class RetailerController {
    @Autowired
    private RetailerService retailerService;

    @PreAuthorize("hasAuthority('READER') or hasAuthority('WRITER')")
    @GetMapping("/{name}/payment-service")
    public ResponseEntity<?> getPaymentServicesForRetailer(@PathVariable("name") String retailer) {
        try {
            return new ResponseEntity<>(retailerService.getPaymentServicesForRetailer(retailer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
