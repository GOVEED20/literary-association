package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.dtos.InitializePaymentRequest;
import goveed20.PaymentConcentrator.exceptions.NotFoundException;
import goveed20.PaymentConcentrator.exceptions.StatusCodeException;
import goveed20.PaymentConcentrator.model.Retailer;
import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import goveed20.PaymentConcentrator.model.Transaction;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.*;
import goveed20.PaymentConcentrator.repositories.RetailerDataForPaymentServiceRepository;
import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import goveed20.PaymentConcentrator.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private FeignClientBuilder feignClientBuilder;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerDataForPaymentServiceRepository retailerDataForPaymentServiceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Set<String> getGlobalPaymentServices() {
        return discoveryClient.getServices()
                .stream()
                .filter(s -> s.matches("^[A-Za-z]+-service$"))
                .collect(Collectors.toSet());
    }

    public Set<String> getRetailerPaymentServices(Long retailerId) {
        Set<String> supportedPaymentServices = retailerDataForPaymentServiceRepository.findByRetailer(retailerId)
                .stream()
                .map(RetailerDataForPaymentService::getPaymentService)
                .collect(Collectors.toSet());

        if (supportedPaymentServices.isEmpty()) {
            throw new NotFoundException(String.format("Retailer with id %d not found", retailerId));
        }
        return supportedPaymentServices;
    }

    public Set<RegistrationField> getPaymentServiceRegistrationFields(String paymentServiceName) {
        if (discoveryClient.getInstances(paymentServiceName).isEmpty()) {
            throw new NotFoundException(String.format("Service with name %s not found", paymentServiceName));
        }

        PluginController service = feignClientBuilder.forType(PluginController.class, paymentServiceName).build();
        ResponseEntity<Set<RegistrationField>> fields = service.getPaymentServiceRegistrationFields();

        if (fields.getStatusCode().isError()) {
            throw new StatusCodeException(fields.getStatusCode());
        }

        return fields.getBody();
    }

    public String initializePayment(String paymentServiceName, InitializePaymentRequest paymentRequest) {
        if (discoveryClient.getInstances(paymentServiceName).isEmpty()) {
            throw new NotFoundException(String.format("Service with name %s not found", paymentServiceName));
        }

        Optional<Retailer> retailerOptional = retailerRepository.findByName(paymentRequest.getRetailer());

        if (retailerOptional.isEmpty()) {
            throw new NotFoundException(String.format("Retailer with name %s not found", paymentRequest.getRetailer()));
        }

        Retailer retailer = retailerOptional.get();
        HashMap<String, String> paymentFields = new HashMap<>();

        retailer.getRetailerDataForPaymentServices()
                .stream()
                .filter(retailerData -> retailerData.getPaymentService().equals(paymentServiceName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Retailer doesn't support payment service %s", paymentServiceName)))
                .getPaymentData()
                .forEach(paymentData -> paymentFields.put(paymentData.getName(), paymentData.getValue()));

        Transaction newTransaction = Transaction.builder()
                .transactionId(paymentRequest.getTransactionId())
                .initializedOn(new Date())
                .amount(paymentRequest.getAmount())
                .paidWith(paymentServiceName)
                .status(goveed20.PaymentConcentrator.model.TransactionStatus.INITIALIZED)
                .build();

        InitializationPaymentPayload payload = InitializationPaymentPayload.childBuilder()
                .paymentFields(paymentFields)
                .transactionId(newTransaction.getTransactionId())
                .amount(paymentRequest.getAmount())
                .build();

        PluginController service = feignClientBuilder.forType(PluginController.class, paymentServiceName).build();
        ResponseEntity<String> redirectionUrlResponse = service.initializePayment(payload);

        if (redirectionUrlResponse.getStatusCode().isError()) {
            throw new StatusCodeException(redirectionUrlResponse.getStatusCode());
        }

        String redirectionUrl = redirectionUrlResponse.getBody();

        try {
            transactionRepository.save(newTransaction);
        } catch (Exception e) {
            throw new StatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return redirectionUrl;
    }

    public void sendTransactionResponse(@RequestBody ResponsePayload responsePayload) {
        Optional<Transaction> transactionOptional = transactionRepository.findByTransactionId(responsePayload.getTransactionID());

        if (transactionOptional.isEmpty()) {
            throw new NotFoundException(String.format("Transaction with transaction id %s not found.", responsePayload.getTransactionID()));
        }

        Transaction transaction = transactionOptional.get();
        if (responsePayload.getTransactionStatus() == TransactionStatus.SUCCESS) {
            transaction.setStatus(goveed20.PaymentConcentrator.model.TransactionStatus.COMPLETED);
        } else {
            transaction.setStatus(goveed20.PaymentConcentrator.model.TransactionStatus.FAILED);
        }

        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new StatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        switch (responsePayload.getTransactionStatus()) {
            case SUCCESS:
                restTemplate.getForEntity(transaction.getSuccessURL(), Void.class);
                break;
            case FAILED:
                restTemplate.getForEntity(transaction.getFailedURL(), Void.class);
                break;
            default:
                restTemplate.getForEntity(transaction.getErrorURL(), Void.class);
        }
    }
}
