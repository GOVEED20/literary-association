package goveed20.LiteraryAssociationApplication.controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        try {
            String url = "http://localhost:8080/api/{retailerName}/payment-services";
            Map<String,String> urlParams = new HashMap<>();
            urlParams.put("retailerName","Laguna");
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "2565a484-e734-4477-bbb0-18e0a0d1afbe");
            ResponseEntity<Object> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(),
                    HttpMethod.GET, new HttpEntity(headers), Object.class);
            return response;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
