package goveed20.BitcoinPaymentService.services;

import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.model.BitcoinOrder;
import goveed20.BitcoinPaymentService.model.BitcoinOrderData;
import goveed20.BitcoinPaymentService.utils.TransactionChecker;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public class PaymentService {
    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    @Autowired
    @Lazy
    private TransactionChecker transactionChecker;

    public String initializePayment(InitializationPaymentPayload payload) throws InterruptedException {

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("coinGateApiKey")) {
            throw new BadRequestException("Missing coingate API Key");
        }

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        BitcoinOrder bitcoinOrder = BitcoinOrder.builder()
                .order_id(payload.getTransactionId().toString())
                .price_amount(payload.getAmount())
                .price_currency("BTC")
                .receive_currency("BTC")
                .title("")
                .description("")
                .callback_url(baseUrl + "/api/complete-payment")
                .cancel_url(payload.getFailedURL())
                .success_url(payload.getSuccessURL())
                .token(UUID.randomUUID().toString())
                .build();

        String clientSecret = "Bearer " + payload.getPaymentFields().get("coinGateApiKey");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", clientSecret);
        String sandboxUrl = "https://api-sandbox.coingate.com/v2/orders";
        ResponseEntity<BitcoinOrder> responseEntity = new RestTemplate().exchange(sandboxUrl, HttpMethod.POST,
                new HttpEntity<>(bitcoinOrder, headers), BitcoinOrder.class);

        if (responseEntity.getBody() == null) {
            throw new BadRequestException("Bitcoin service is not available");
        }
        String paymentUrl = responseEntity.getBody().getPayment_url();

        if (paymentUrl == null || paymentUrl.equals("")) {
            throw new BadRequestException("Missing payment url from coingate");
        }

        transactionChecker.checkTransaction(responseEntity.getBody().getId(), payload.getPaymentFields().get("coinGateApiKey"));

        return paymentUrl;
    }

    public void completePayment(BitcoinOrderData data) {

        Long transactionId;
        try {
            transactionId = Long.parseLong(data.getOrder_id());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Transaction id has wrong format");
        }

        if (data.getStatus().equals("paid")) {
            sendTransactionResponse(transactionId, TransactionStatus.SUCCESS);
        } else if (data.getStatus().equals("expired") || data.getStatus().equals("canceled")) {
            sendTransactionResponse(transactionId, TransactionStatus.FAILED);
        } else {
            sendTransactionResponse(transactionId, TransactionStatus.ERROR);
        }
    }

    @Async
    @SneakyThrows
    public void sendTransactionResponse(Long transactionId, TransactionStatus status) {
        paymentConcentratorFeignClient.sendTransactionResponse(
                ResponsePayload.builder()
                        .transactionID(transactionId)
                        .transactionStatus(status)
                        .build()
        );
    }

    public Set<RegistrationField> getPaymentServiceRegistrationFields() {
        Map<String, String> validationConstraints = new HashMap<>();
        validationConstraints.put("type", "text");
        validationConstraints.put("required", "required");

        RegistrationField coinGateApiKey = RegistrationField.builder()
                .encrypted(true)
                .name("coinGateApiKey")
                .validationConstraints(validationConstraints)
                .build();

        Set<RegistrationField> registrationFields = new HashSet<>();
        registrationFields.add(coinGateApiKey);

        return registrationFields;
    }
}
