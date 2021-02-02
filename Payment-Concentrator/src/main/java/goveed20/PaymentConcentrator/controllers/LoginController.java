package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.LoginData;
import goveed20.PaymentConcentrator.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PreAuthorize("permitAll()")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> login(@RequestPart("loginData") LoginData loginData) {
        try {
            return new ResponseEntity<>(loginService.login(loginData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
