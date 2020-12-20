package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping(value = "/api")
public interface PluginController {

    @PostMapping(value = "/initialize-payment")
    ResponseEntity<?> initializePayment(@Valid @RequestBody InitializationPaymentPayload payload);

    @PostMapping(value = "/complete-payment")
    ResponseEntity<?> completePayment(@Valid @RequestBody TransactionDataPayload payload);

    /*
        Payment Concentrator should call this endpoint during Retailer registration to get necessary fields. Fields
        should be saved in RetailerDataForPaymentService class
    * */
    @GetMapping(value = "/payment-service/registration-fields")
    ResponseEntity<Set<RegistrationField>> getPaymentServiceRegistrationFields();

}
