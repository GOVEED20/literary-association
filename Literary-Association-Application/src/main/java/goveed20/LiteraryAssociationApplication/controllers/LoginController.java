package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.LoginDTO;
import goveed20.LiteraryAssociationApplication.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    @PreAuthorize("!(hasAuthority('READER') or hasAuthority('WRITER') or hasAuthority('LECTOR') or hasAuthority('BOARD_MEMBER') or hasAuthority('EDITOR'))")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginData) {
        try {
            return new ResponseEntity<>(loginService.login(loginData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}
