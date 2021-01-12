package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "logging")
public interface LoggingFeignClient {

    @PostMapping()
    ResponseEntity<?> createLog(@RequestBody LogDTO logDTO);

}
