package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.OrderDTO;
import goveed20.LiteraryAssociationApplication.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> pay(@RequestBody OrderDTO order) {
        return new ResponseEntity<>(transactionService.pay(order), HttpStatus.OK);
    }

    @GetMapping("/success")
    public void transactionSuccess() {
        System.out.println("Success");
    }

    @GetMapping("/failed")
    public void transactionFailed() {
        System.out.println("Failed");
    }

    @GetMapping("/error")
    public void transactionError() {
        System.out.println("Error");
    }


}
