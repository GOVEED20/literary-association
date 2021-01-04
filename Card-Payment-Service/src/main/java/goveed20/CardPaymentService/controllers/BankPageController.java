package goveed20.CardPaymentService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class BankPageController {

    @GetMapping(value = "/bank-page/{bankName}/{paymentID}/{transactionID}")
    public String getBankPage(@PathVariable String bankName, @PathVariable String paymentID, Model model,
                              @PathVariable String transactionID) {
        model.addAttribute("bankName", bankName);
        model.addAttribute("paymentID", paymentID);
        model.addAttribute("baseURL", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        model.addAttribute("transactionID", transactionID);

        return "bankPage";
    }

}
