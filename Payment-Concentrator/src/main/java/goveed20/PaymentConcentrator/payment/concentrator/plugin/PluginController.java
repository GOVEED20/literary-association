package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@RequestMapping(value = "/api")
public interface PluginController {

    @PostMapping(value = "/initialize-payment")
    ResponseEntity<String> initializePayment(@Valid @RequestBody InitializationPaymentPayload payload);

    @GetMapping(value = "/complete-payment/{transactionId}")
    ResponseEntity<?> completePaymentGet(HttpServletRequest request);

    @GetMapping(value = "/payment-service/registration-fields")
    ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields();

}
