package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api")
public interface PluginPageController {

    @GetMapping("/payment/{id}")
    ResponseEntity getPluginPage(@PathVariable String id);

    @PostMapping("/payment")
    ResponseEntity submitTransactionData(@Valid @RequestBody TransactionDataPayload payload);
}
