package goveed20.BitcoinPaymentService.aspects;

import goveed20.BitcoinPaymentService.model.BitcoinOrderData;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.AsyncLogging;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AsyncLogging asyncLogging;

    @Before("execution(public * goveed20.BitcoinPaymentService.services.PaymentService.*(..)) ||" +
            "execution(public * goveed20.BitcoinPaymentService.services.RetailerService.*(..)) ||" +
            "execution(* goveed20.BitcoinPaymentService.controllers.*.*(..))")
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

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    @AfterReturning("execution(public * goveed20.BitcoinPaymentService.services.PaymentService.*(..)) ||" +
                    "execution(public * goveed20.BitcoinPaymentService.services.RetailerService.*(..))")
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

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    @AfterThrowing(pointcut = "execution(public * goveed20.BitcoinPaymentService.services.PaymentService.*(..)) ||" +
            "execution(public * goveed20.BitcoinPaymentService.services.RetailerService.*(..)) ||" +
            "execution(* goveed20.BitcoinPaymentService.controllers.*.*(..))", throwing = "error")
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

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    private LogDTO generateLog(String className, String methodName, String logLevel, String message) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return LogDTO.builder()
                .date(formatter.parse(formatter.format(new Date())))
                .serviceName("bitcoin-service")
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

    private String generateServiceMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Initialize bitcoin transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Bitcoin transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " successfully initialized";
                break;
            case "completePayment":
                BitcoinOrderData bitcoinOrderData = (BitcoinOrderData) arguments[0];
                message = isBefore ?
                        "Complete bitcoin transaction with order id " + bitcoinOrderData.getOrder_id() +
                                " , got status " + bitcoinOrderData.getStatus()
                        :
                        "Bitcoin transaction with order id " + bitcoinOrderData.getOrder_id() +
                                " completed with status " + bitcoinOrderData.getStatus();
                break;
            case "sendTransactionResponse":
                Long transactionId = (Long) arguments[0];
                TransactionStatus status = (TransactionStatus) arguments[1];
                message = isBefore ?
                        "Sending data of bitcoin transaction with id " + transactionId + " and status " +
                                status + " to payment concentrator"
                        :
                        "Data of bitcoin transaction with id " + transactionId + " and status " + status +
                                " sent to payment concentrator";
                break;
            case "getPaymentServiceRegistrationFields":
            message = isBefore ?
                    "Started getting registration fields on bitcoin service"
                    :
                    "Bitcoin service registration fields successfully supplied";
            break;
            case "checkPaymentServiceFields":
                message = isBefore ?
                        "Checking registration fields on bitcoin service"
                        :
                        "Bitcoin service registration fields successfully checked";
                break;
            default:
                message = "";
        }

        return message;
    }

    private String generateControllerMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Starting initializating bitcoin transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Successfully initialized bitcoin transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount();
                break;
            case "getPaymentServiceRegistrationFields":
                message = isBefore ?
                        "Starting getting registration fields for bitcoin service "
                        :
                        "Successfully got registration fields for bitcoin service";
                break;
            case "checkPaymentServiceFields":
                message = isBefore ?
                        "Starting checking registration fields on bitcoin service"
                        :
                        "Successfully checked registration fields on bitcoin service";
                break;
            default:
                message = "";

        }

        return message;
    }

}
