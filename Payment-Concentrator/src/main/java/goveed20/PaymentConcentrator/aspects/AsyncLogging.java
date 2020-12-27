package goveed20.PaymentConcentrator.aspects;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LoggingFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncLogging {

    @Autowired
    private LoggingFeignClient loggingFeignClient;

    @Async
    public void callFeignClient(LogDTO logDTO) {
        loggingFeignClient.createLog(logDTO);
    }

}
