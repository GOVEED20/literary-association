package goveed20.BitcoinPaymentService.services;

import com.google.gson.Gson;
import goveed20.BitcoinPaymentService.dtos.CompleteBitcoinOrder;
import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.model.BitcoinOrder;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    public String initializePayment(InitializationPaymentPayload payload) {

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

        System.out.print(bitcoinOrder.getCallback_url());

        String clientSecret = "Bearer " + payload.getPaymentFields().get("coinGateApiKey");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", clientSecret);
        String sandboxUrl = "https://api-sandbox.coingate.com/v2/orders";
        ResponseEntity<BitcoinOrder> responseEntity = new RestTemplate().exchange(sandboxUrl, HttpMethod.POST,
                new HttpEntity<BitcoinOrder>(bitcoinOrder, headers), BitcoinOrder.class);

        String paymentUrl = responseEntity.getBody().getPayment_url();

        if (paymentUrl == null || paymentUrl.equals("")) {
            throw new BadRequestException("Missing payment url from coingate");
        }

        log.info("BTC PaymentService: Received payment_url for transaction with id " +
                payload.getTransactionId());

        return responseEntity.getBody().getPayment_url();
    }

    public void completePayment(String data) {
        Gson gson = new Gson();
        CompleteBitcoinOrder completedPayment = gson.fromJson(data, CompleteBitcoinOrder.class);

        log.info("BTC PaymentService: Started completing transaction with id " +
                completedPayment.getOrder_id());

        if (!completedPayment.getStatus().equals("paid") && !completedPayment.getStatus().equals("refunded")) {
            log.info("BTC PaymentService: Transaction with id " +
                    completedPayment.getOrder_id() + " got status FAILED");
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(UUID.fromString(completedPayment.getOrder_id()))
                            .transactionStatus(TransactionStatus.FAILED)
                            .build()
            );
        }
        else {
            log.info("BTC PaymentService: Transaction with id " +
                    completedPayment.getOrder_id() + " got status SUCCESS");
            paymentConcentratorFeignClient.sendTransactionResponse(
                    ResponsePayload.childBuilder()
                            .transactionID(UUID.fromString(completedPayment.getOrder_id()))
                            .transactionStatus(TransactionStatus.SUCCESS)
                            .build()
            );
        }
    }
}
