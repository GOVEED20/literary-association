package goveed20.PaymentConcentrator.payment.concentrator.plugin;

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
    public void callLoggingFeignClient(LogDTO logDTO) {
        loggingFeignClient.createLog(logDTO);
    }

    @Async
    public void callLoggingFeignClient(String serviceName, String className, String methodName, String logLevel, String message) {
        LogDTO logDTO = LogDTO.builder()
                .serviceName(serviceName)
                .className(className)
                .methodName(methodName)
                .logLevel(logLevel)
                .message(message)
                .build();
        loggingFeignClient.createLog(logDTO);
    }

}
