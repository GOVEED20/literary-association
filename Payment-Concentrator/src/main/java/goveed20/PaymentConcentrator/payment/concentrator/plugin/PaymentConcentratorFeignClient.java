package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-concentrator")
public interface PaymentConcentratorFeignClient {

    @PostMapping("/api/payment")
    ResponseEntity<ResponsePayload> sendTransactionResponse();
}
