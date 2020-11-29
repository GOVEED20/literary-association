package goveed20.PaymentConcentrator.controller;

import goveed20.PaymentConcentrator.client.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    GatewayClient gatewayClient;

    @PostMapping
    public ResponseEntity<?> initializePayment(@RequestBody String paymentType) {

        if(paymentType.equals("Card payment")) {
            return new ResponseEntity<>(gatewayClient.payWithCard(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid payment type", HttpStatus.BAD_REQUEST);

    }

}
