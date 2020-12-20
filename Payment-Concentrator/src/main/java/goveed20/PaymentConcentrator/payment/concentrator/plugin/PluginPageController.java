package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface PluginPageController {

    @GetMapping("/payment/{id}")
    String getPluginPage(@PathVariable String id);
}
