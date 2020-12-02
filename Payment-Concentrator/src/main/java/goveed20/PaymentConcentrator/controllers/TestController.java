package goveed20.PaymentConcentrator.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "payment-concentrator")
public class TestController {
    @Value("${spring.cloud.consul.discovery.instance-id}")
    private String id;

    @GetMapping(value = "test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
