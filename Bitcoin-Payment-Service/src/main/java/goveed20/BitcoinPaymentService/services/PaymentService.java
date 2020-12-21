package goveed20.BitcoinPaymentService.services;

import com.google.gson.Gson;
import goveed20.BitcoinPaymentService.dtos.CompleteBitcoinOrder;
import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.model.BitcoinOrder;
import goveed20.BitcoinPaymentService.model.BitcoinOrderData;
import goveed20.BitcoinPaymentService.utils.BitcoinTransactionChecker;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PaymentConcentratorFeignClient;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    @Autowired
    private BitcoinTransactionChecker bitcoinTransactionChecker;

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

        String paymentUrl = responseEntity.getBody().getPayment_url();

        if (paymentUrl == null || paymentUrl.equals("")) {
            throw new BadRequestException("Missing payment url from coingate");
        }

        bitcoinTransactionChecker.checkTransaction(responseEntity.getBody().getId(), payload.getPaymentFields().get("coinGateApiKey"));

        log.info("BTC PaymentService: Received payment_url for transaction with id " +
                payload.getTransactionId());

        return paymentUrl;
    }

    public void completePayment(BitcoinOrderData data) {

        log.info("BTC PaymentService: Started completing transaction with id " +
                data.getOrder_id());

        if (data.getStatus().equals("paid")) {
            log.info("BTC PaymentService: Transaction with id " +
                    data.getOrder_id() + " got status SUCCESS");
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(UUID.fromString(data.getOrder_id()))
                            .transactionStatus(TransactionStatus.SUCCESS)
                            .build()
            );
        }
        else if (data.getStatus().equals("expired") || data.getStatus().equals("canceled")){
            log.info("BTC PaymentService: Transaction with id " +
                    data.getOrder_id() + " got status FAILED");
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(UUID.fromString(data.getOrder_id()))
                            .transactionStatus(TransactionStatus.FAILED)
                            .build()
            );
        }
        else {
            log.info("BTC PaymentService: Transaction with id " +
                    data.getOrder_id() + " got status ERROR");
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(UUID.fromString(data.getOrder_id()))
                            .transactionStatus(TransactionStatus.ERROR)
                            .build()
            );
        }
    }
}
