package goveed20.PaymentConcentrator.controllers;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "payment-concentrator")
@Slf4j
public class TestController {
    @Value("${spring.cloud.consul.discovery.instance-id}")
    private String id;

    @GetMapping(value = "test")
    public ResponseEntity<String> test() {
        log.info("Logovanje");
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
