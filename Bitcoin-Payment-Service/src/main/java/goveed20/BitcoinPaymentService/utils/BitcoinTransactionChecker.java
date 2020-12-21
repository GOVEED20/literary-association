package goveed20.BitcoinPaymentService.utils;

import goveed20.BitcoinPaymentService.model.BitcoinOrderData;
import goveed20.BitcoinPaymentService.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BitcoinTransactionChecker {

    @Autowired
    private PaymentService paymentService;

    @Async
    public void checkTransaction(Integer id, String coingateApiKey) throws InterruptedException {
        String getOrderSandboxUrl = "https://api-sandbox.coingate.com/v2/orders/" + id;

        String clientSecret = "Bearer " + coingateApiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", clientSecret);

        ResponseEntity<BitcoinOrderData> responseEntity = null;

        do {
            responseEntity = new RestTemplate().exchange(getOrderSandboxUrl, HttpMethod.GET,
                    new HttpEntity<>(headers), BitcoinOrderData.class);
            Thread.sleep(5000);

        } while(responseEntity.getBody().getStatus().equals("new") || responseEntity.getBody().getStatus().equals("pending") ||
                responseEntity.getBody().getStatus().equals("confirming"));

        paymentService.completePayment(responseEntity.getBody());
    }

}
