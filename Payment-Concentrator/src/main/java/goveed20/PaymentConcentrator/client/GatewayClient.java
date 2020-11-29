package goveed20.PaymentConcentrator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Gateway")
public interface GatewayClient {

    @PostMapping(value = "/api/card-payment")
    String payWithCard();

}
