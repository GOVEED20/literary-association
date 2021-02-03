package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.InvoiceDTO;
import goveed20.LiteraryAssociationApplication.services.TransactionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public void initializeTransaction(@RequestBody @Valid InvoiceDTO invoiceDTO, HttpServletResponse response) {
        try {
            response.sendRedirect(transactionService.initializeTransaction(invoiceDTO));
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @GetMapping("/{id}/{status}")
    public ResponseEntity<?> completeTransaction(@PathVariable("id") Long id, @PathVariable("status") String status) {
        transactionService.completeTransaction(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
