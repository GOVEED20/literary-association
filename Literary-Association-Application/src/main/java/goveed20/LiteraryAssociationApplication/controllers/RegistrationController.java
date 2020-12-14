package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/form-fields")
    public ResponseEntity<FormFieldsDTO> getFormFields() {
        return new ResponseEntity<>(registrationService.getFormFields(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody FormSubmissionDTO regData) {
        registrationService.register(regData);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verify() {
        registrationService.verify();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
