package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/api")
public interface PluginRetailerController {

    @PostMapping(value = "/check-fields")
    ResponseEntity<String> checkPaymentServiceFields(@RequestBody List<RegistrationFieldForm> payload);
}
