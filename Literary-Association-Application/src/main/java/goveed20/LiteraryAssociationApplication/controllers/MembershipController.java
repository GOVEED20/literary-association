package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.InvoiceDTO;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.services.MembershipService;
import goveed20.LiteraryAssociationApplication.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/membership")
@CrossOrigin
public class MembershipController {
    @Autowired
    private MembershipService membershipService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getActiveMembership(@RequestParam("username") String username) {
        try {
            return new ResponseEntity<>(membershipService.getActiveMembership(username), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> initializeMembershipTransaction(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        try {
            return new ResponseEntity<>(transactionService.initializeTransaction(invoiceDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
