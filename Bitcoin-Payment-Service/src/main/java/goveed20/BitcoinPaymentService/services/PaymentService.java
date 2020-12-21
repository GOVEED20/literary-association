package goveed20.BitcoinPaymentService.services;

import com.google.gson.Gson;
import goveed20.BitcoinPaymentService.dtos.CompleteBitcoinOrder;
import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.BitcoinPaymentService.model.BitcoinOrder;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Service
public class PaymentService {

    private final String sandboxUrl = "https://api-sandbox.coingate.com/v2/orders";

    public String initializePayment(InitializationPaymentPayload payload) {

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("coinGateApiKey")) {
            throw new BadRequestException("Missing coingate API Key");
        }

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        baseUrl = baseUrl.replace("host.docker.internal", "bitcoin-service");

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
        ResponseEntity<BitcoinOrder> responseEntity = new RestTemplate().exchange(sandboxUrl, HttpMethod.POST,
                new HttpEntity<BitcoinOrder>(bitcoinOrder, headers), BitcoinOrder.class);

        return responseEntity.getBody().getPayment_url();
    }

    public void completePayment(String data) {
        Gson gson = new Gson();
        CompleteBitcoinOrder completedPayment = gson.fromJson(data, CompleteBitcoinOrder.class);
        
    }
}
