package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/form-fields/{processID}")
    public ResponseEntity<FormFieldsDTO> getFormFields(@PathVariable String processID) {
        return new ResponseEntity<>(registrationService.getFormFields(processID), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody FormSubmissionDTO regData) {
        registrationService.register(regData);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/verification/{pID}")
    public ResponseEntity<?> verify(@RequestParam("token") String disHash, @PathVariable String pID) throws Exception {
        registrationService.verify(disHash, pID);
        return new ResponseEntity<>("Your account is successfully verified", HttpStatus.OK);
    }
}
