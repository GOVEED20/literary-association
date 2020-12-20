package goveed20.PaypalPaymentService.controllers;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginPageController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController implements PluginPageController {
    @Override
    public String getPluginPage(String id) {
        return null;
    }
}
