package goveed20.LiteraryAssociationApplication.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import goveed20.LiteraryAssociationApplication.configs.YAMLConfig;
import goveed20.LiteraryAssociationApplication.dtos.RetailerData;
import goveed20.LiteraryAssociationApplication.exceptions.PaymentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    private final RestTemplate restTemplate = new RestTemplateBuilder().errorHandler(new RestTemplateResponseErrorHandler()).build();

    private final String baseUrl = "http://localhost:8080/api";

    @Autowired
    private YAMLConfig myConfig;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", myConfig.getRegistrationToken());

        return headers;
    }

    public Object getAvailableServices(String retailerName) throws PaymentException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/{retailerName}/payment-services");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("retailerName", retailerName == null ? myConfig.getName() : retailerName);
        ResponseEntity<Object> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(),
                HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);

        return response.getBody();
    }

    public Object getServiceRegistrationFields(String serviceName) throws PaymentException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/payment-services/{paymentService}/registration-fields");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("paymentService", serviceName);
        ResponseEntity<Object> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(),
                HttpMethod.GET, new HttpEntity<>(getHeaders()), Object.class);

        return response.getBody();
    }

    public void sendRegisterRetailerRequest(RetailerData retailerData) throws PaymentException {
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
                String.class);
    }
}
