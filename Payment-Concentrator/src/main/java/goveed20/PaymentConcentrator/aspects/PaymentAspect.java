package goveed20.PaymentConcentrator.aspects;

import goveed20.PaymentConcentrator.dtos.InitializePaymentRequest;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.AsyncLogging;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class PaymentAspect {

    @Autowired
    private AsyncLogging asyncLogging;

    @Before("execution(public * goveed20.PaymentConcentrator.services.PaymentService.*(..)) || " +
            "execution(* goveed20.PaymentConcentrator.controllers.*.*(..))")
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

    @AfterReturning("execution(public * goveed20.PaymentConcentrator.services.PaymentService.*(..))")
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

    @AfterThrowing(pointcut = "execution(public * goveed20.PaymentConcentrator.services.PaymentService.*(..)) || " +
            "execution(* goveed20.PaymentConcentrator.controllers.*.*(..))", throwing = "error")
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
                .serviceName("payment-concentrator")
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
                InitializePaymentRequest initializePaymentRequest = (InitializePaymentRequest) arguments[1];
                message = isBefore ?
                        "Starting initializating transaction with id " + initializePaymentRequest.getTransactionId()
                                + " and amount " + initializePaymentRequest.getAmount()
                        :
                        "Successfully initialized transaction with id " + initializePaymentRequest.getTransactionId()
                                + " and amount " + initializePaymentRequest.getAmount();
                break;
            case "sendTransactionResponse":
                ResponsePayload responsePayload = (ResponsePayload) arguments[0];
                message = isBefore ?
                        "Starting finishing transaction with id " + responsePayload.getTransactionID() + " and status "
                                + responsePayload.getTransactionStatus().toString()
                        :
                        "Successfully completed transaction with id " + responsePayload.getTransactionID() +
                                ", got status " + responsePayload.getTransactionStatus().toString();
                break;
            case "getPaymentServiceRegistrationFields":
                String serviceName = (String) arguments[0];
                message = isBefore ?
                        "Started getting registration fields for payment service with name " + serviceName
                        :
                        "Successfully got registration fields for payment service with name " + serviceName;
                break;
            case "getRetailerPaymentServices":
                Long retailerId = (Long) arguments[0];
                message = isBefore ?
                        "Started getting payment services of retailer with id " + retailerId
                        :
                        "Successfully got payment services of retailer with id " + retailerId;
                break;
            case "getGlobalPaymentServices":
                message = isBefore ?
                        "Started getting all available payment services"
                        :
                        "Successfully got all available payment services";
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
                InitializePaymentRequest initializePaymentRequest = (InitializePaymentRequest) arguments[1];
                message = isBefore ?
                        "Initialize transaction with id " + initializePaymentRequest.getTransactionId()
                            + " and amount " + initializePaymentRequest.getAmount()
                        :
                        "Transaction with id " + initializePaymentRequest.getTransactionId()
                            + " and amount " + initializePaymentRequest.getAmount() + " successfully initialized";

                break;
            case "sendTransactionResponse":
                ResponsePayload responsePayload = (ResponsePayload) arguments[0];
                message = isBefore ?
                        "Finish transaction with id " + responsePayload.getTransactionID() + " and status "
                            + responsePayload.getTransactionStatus().toString()
                        :
                        "Transaction with id " + responsePayload.getTransactionID() +
                                " completed with status " + responsePayload.getTransactionStatus().toString();
                break;
            case "getPaymentServiceRegistrationFields":
                String serviceName = (String) arguments[0];
                message = isBefore ?
                        "Getting registration fields for payment service with name " + serviceName
                        :
                        "Got registration fields for payment service with name " + serviceName;
                break;
            case "getRetailerPaymentServices":
                Long retailerId = (Long) arguments[0];
                message = isBefore ?
                        "Getting payment services of retailer with id " + retailerId
                        :
                        "Got payment services of retailer with id " + retailerId;
                break;
            case "getGlobalPaymentServices":
                message = isBefore ?
                        "Started getting all available payment services"
                        :
                        "Got all available payment services";
                break;
            default:
                message = "";
        }
        
        return message;
    }


}
