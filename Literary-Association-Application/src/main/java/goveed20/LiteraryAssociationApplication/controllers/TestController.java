package goveed20.LiteraryAssociationApplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "/transaction/{transactionId}/{status}")
    public ResponseEntity<?> transactionCallback(@PathVariable("transactionId") String transactionid, @PathVariable("status") String status) {
        System.out.println("Transaction id: " + transactionid);
        System.out.println("Status: " + status);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
