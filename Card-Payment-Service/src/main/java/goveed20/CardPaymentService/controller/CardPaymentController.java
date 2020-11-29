package goveed20.CardPaymentService.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card-payment")
public class CardPaymentController {

    @PostMapping
    public String pay() {
        return "Transaction successfully processed";
    }

}
