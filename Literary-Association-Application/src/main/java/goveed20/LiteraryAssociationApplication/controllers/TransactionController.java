package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.InvoiceDTO;
import goveed20.LiteraryAssociationApplication.services.TransactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @SneakyThrows
    @PreAuthorize("hasAuthority('READER') or hasAuthority('WRITER')")
    @PostMapping
    public ResponseEntity<?> initializeTransaction(@RequestBody @Valid InvoiceDTO invoiceDTO) {
        try {
            return new ResponseEntity<>(transactionService.initializeTransaction(invoiceDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/{status}")
    public ResponseEntity<?> completeTransaction(@PathVariable("id") Long id, @PathVariable("status") String status) {
        transactionService.completeTransaction(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
