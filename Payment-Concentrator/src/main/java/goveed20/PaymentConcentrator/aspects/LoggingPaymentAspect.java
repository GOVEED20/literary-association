package goveed20.PaymentConcentrator.aspects;

import goveed20.PaymentConcentrator.dtos.InitializePaymentRequest;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LoggingFeignClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Aspect
@Component
public class LoggingPaymentAspect {

    @Autowired
    private LoggingFeignClient loggingFeignClient;

    @Before("execution(public * goveed20.PaymentConcentrator.services.PaymentService.*(..))")
    public void paymentService(JoinPoint joinPoint) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        LogDTO logDTO = null;
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = generateMessage(methodName, arguments, true);
        try {
            logDTO = LogDTO.builder()
                    .date(formatter.parse(formatter.format(new Date())))
                    .serviceName("bitcoin-service")
                    .className(className)
                    .methodName(methodName)
                    .logLevel("INFO")
                    .message(message)
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loggingFeignClient.createLog(logDTO);
    }

    private String generateMessage(String methodName, Object[] arguments, boolean isBefore) {
        StringBuilder sb = new StringBuilder();
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializePaymentRequest payload = (InitializePaymentRequest) arguments[1];
                message = "Initialize transaction with id " + payload.getTransactionId()
                        + " and amount " + payload.getAmount();
                break;
            case "sendTransactionResponse":
                message = "";
                break;
            default:
                message = "";
        }
        
        return message;
    }


}
