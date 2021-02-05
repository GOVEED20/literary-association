package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/register")
@PreAuthorize("!(hasAuthority('READER') or hasAuthority('WRITER') or hasAuthority('LECTOR') or hasAuthority('BOARD_MEMBER') or hasAuthority('EDITOR'))")
@CrossOrigin
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/form-fields/{processID}")
    public ResponseEntity<FormFieldsDTO> getFormFields(@PathVariable String processID) {
        return new ResponseEntity<>(registrationService.getFormFields(processID), HttpStatus.OK);
    }

    @GetMapping("/verification/{pID}")
    public void verify(@RequestParam("token") String disHash, @PathVariable String pID, HttpServletResponse response) throws IOException {
        registrationService.verify(disHash, pID);
        response.sendRedirect("http://localhost:3000/login");
    }
}
