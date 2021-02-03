package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.exceptions.PaymentException;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class RetailerService {
    @Autowired
    private RetailerRepository retailerRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Set<String> getPaymentServicesForRetailer(String retailerName) {
        if (retailerRepository.findByName(retailerName).isEmpty()) {
            throw new NotFoundException(String.format("Retailer with name '%s' not found", retailerName));
        }

        ParameterizedTypeReference<Set<String>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Set<String>> response = restTemplate.exchange(String.format("http://localhost:8080/api/%s/payment-services", retailerName), HttpMethod.GET, null, responseType);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new PaymentException(response.getStatusCode().getReasonPhrase());
        }

        return response.getBody();
    }
}
