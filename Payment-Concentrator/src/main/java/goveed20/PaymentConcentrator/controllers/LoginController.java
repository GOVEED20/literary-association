package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.LoginData;
import goveed20.PaymentConcentrator.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", consumes = "multipart/form-data")
    public ResponseEntity<?> login(@RequestPart("loginData") LoginData loginData) {
        try {
            return new ResponseEntity<>(loginService.login(loginData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
