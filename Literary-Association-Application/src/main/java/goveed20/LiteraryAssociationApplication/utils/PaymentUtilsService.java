package goveed20.LiteraryAssociationApplication.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import goveed20.LiteraryAssociationApplication.configs.YAMLConfig;
import goveed20.LiteraryAssociationApplication.dtos.RetailerData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentUtilsService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl = "http://localhost:8080/api";

    @Autowired
    private YAMLConfig myConfig;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", myConfig.getRegistrationToken());

        return headers;
    }

    public ResponseEntity<Object> getAvailableServices() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/{retailerName}/payment-services");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("retailerName", myConfig.getName());
        return restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(),
                HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);
    }

    public ResponseEntity<Object> getServiceRegistrationFields(String serviceName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/payment-services/{paymentService}/registration-fields");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("paymentService", serviceName);
        return restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(),
                HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);
    }

    public void sendRegisterRetailerRequest(RetailerData retailerData) {
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);

        JSONObject payload = new JSONObject();
        payload.put("retailerName", retailerData.getRetailerName());
        payload.put("retailerEmail", retailerData.getRetailerEmail());
        payload.put("paymentServices", retailerData.getPaymentServices());

        restTemplate.exchange(
                baseUrl + "/register-external",
                HttpMethod.POST,
                new HttpEntity<>(payload.toMap(), getHeaders()),
                String.class
        );
    }

}
