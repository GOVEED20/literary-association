package goveed20.LiteraryAssociationApplication.services;

import com.fasterxml.jackson.databind.SerializationFeature;
import goveed20.LiteraryAssociationApplication.dtos.OrderDTO;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;

@Service
public class TransactionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String pay(OrderDTO order) {
        JSONObject jo = new JSONObject();
        jo.put("retailer", "Laguna");
        jo.put("amount", order.getAmount());
        jo.put("transactionId", new Random().nextLong());
        jo.put("successURL", "http://localhost:9090/api/transactions/success");
        jo.put("failedURL", "http://localhost:9090/api/transactions/failed");
        jo.put("errorURL", "http://localhost:9090/api/transactions/error");

        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/payment-services/" + order.getPaymentService() + "/initialize-payment",
                jo.toMap(), String.class);

        return response.getBody();
    }
}
