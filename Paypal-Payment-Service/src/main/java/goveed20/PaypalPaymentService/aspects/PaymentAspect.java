package goveed20.PaypalPaymentService.aspects;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.net.http.HttpRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class PaymentAspect {

    @Autowired
    private AsyncLogging asyncLogging;

    @Before("execution(public * goveed20.PaypalPaymentService.services.PaymentService.*(..)) || " +
            "execution(* goveed20.PaypalPaymentService.controllers.*.*(..))")
    public void paymentBefore(JoinPoint joinPoint) {
        LogDTO logDTO = null;
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = generateMessage(className, methodName, arguments, true);
        try {
            logDTO = generateLog(className, methodName, "INFO", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callFeignClient(logDTO);
    }

    @AfterReturning("execution(public * goveed20.PaypalPaymentService.services.PaymentService.*(..))")
    public void paymentServiceAfterSuccess(JoinPoint joinPoint) {
        LogDTO logDTO = null;
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = generateMessage(className, methodName, arguments, false);
        try {
            logDTO = generateLog(className, methodName, "INFO", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callFeignClient(logDTO);
    }

    @AfterThrowing(pointcut = "execution(public * goveed20.PaypalPaymentService.services.PaymentService.*(..)) || " +
            "execution(* goveed20.PaypalPaymentService.controllers.*.*(..))", throwing = "error")
    public void paymentServiceAfterError(JoinPoint joinPoint, Throwable error) {

        LogDTO logDTO = null;
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = error.getMessage();
        try {
            logDTO = generateLog(className, methodName, "ERROR", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callFeignClient(logDTO);
    }

    private LogDTO generateLog(String className, String methodName, String logLevel, String message) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return LogDTO.builder()
                .date(formatter.parse(formatter.format(new Date())))
                .serviceName("paypal-service")
                .className(className)
                .methodName(methodName)
                .logLevel(logLevel)
                .message(message)
                .build();
    }

    private String generateMessage(String className, String methodName, Object[] arguments, boolean isBefore) {
        String returnMessage;
        returnMessage = className.equals("PaymentController") ?
                generateControllerMessage(methodName, arguments, isBefore)
                :
                generateServiceMessage(methodName, arguments, isBefore);

        return returnMessage;
    }

    private String generateControllerMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Starting initializating paypal transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Successfully initialized paypal transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount();
                break;
            case "completePayment":
                message = isBefore ?
                        "Starting completing paypal transaction"
                        :
                        "Successfully completed paypal transaction";
                break;
            default:
                message = "";
        }

        return message;
    }

    private String generateServiceMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Initialize paypal transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Paypal transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " successfully initialized";
                break;
            case "completePayment":
                Long payPalTransactionId = (Long) arguments[0];
                message = isBefore ?
                        "Complete paypal transaction with id " + payPalTransactionId
                        :
                        "Paypal transaction with id " + payPalTransactionId +
                                " completed";
                break;
            case "sendTransactionResponse":
                Long transactionId = (Long) arguments[0];
                TransactionStatus status = (TransactionStatus) arguments[1];
                message = isBefore ?
                        "Sending data of paypal transaction with id " + transactionId + " and status " +
                                status + " to payment concentrator"
                        :
                        "Data of paypal transaction with id " + transactionId + " and status " + status +
                                " sent to payment concentrator";
                break;
            default:
                message = "";
        }

        return  message;
    }

}
