package goveed20.PaypalPaymentService.services;

import com.paypal.base.rest.APIContext;
import goveed20.PaypalPaymentService.dtos.*;
import goveed20.PaypalPaymentService.exceptions.SubscriptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("deprecation")
@Service
public class PaypalSubscriptionsService {
    @Autowired
    private APIContext apiContext;

    private final RestTemplate restTemplate = new RestTemplate();

    static final String PRODUCTS_URL = "https://api-m.sandbox.paypal.com/v1/catalogs/products";
    static final String PLANS_URL = "https://api-m.sandbox.paypal.com/v1/billing/plans";
    static final String SUBSCRIPTIONS_URL = "https://api-m.sandbox.paypal.com/v1/billing/subscriptions";
    static final String GET_SUBSCRIPTION_URL = "https://api.sandbox.paypal.com/v1/billing/subscriptions/%s";

    public String createProduct(String name) {
        PaypalProductRequest product = PaypalProductRequest.builder()
                .name(name)
                .type("SERVICE")
                .category("MEMBERSHIP_CLUBS_AND_ORGANIZATIONS")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());
        headers.add("Prefer", "return=minimal");

        HttpEntity<PaypalProductRequest> request = new HttpEntity<>(product, headers);

        ResponseEntity<IdDTO> response = restTemplate.postForEntity(PRODUCTS_URL, request, IdDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getId();
        } else {
            throw new SubscriptionException("Failed to create product");
        }
    }

    public String createPlan(String productId, String amount) {
        PaypalPlanRequest plan = PaypalPlanRequest.builder()
                .product_id(productId)
                .name(String.format("Plan for %s", productId))
                .billing_cycle(
                        BillingCycleDTO.builder()
                                .frequency(
                                        FrequencyDTO.builder()
                                                .interval_unit("MONTH")
                                                .interval_count(1)
                                                .build()
                                )
                                .tenure_type("REGULAR")
                                .sequence(1)
                                .total_cycles(0)
                                .pricing_scheme("fixed_price",
                                        PriceDTO.builder()
                                                .value(amount)
                                                .currency_code("USD")
                                                .build()
                                )
                                .build()
                )
                .payment_preference("auto_bill_outstanding", true)
                .payment_preference("payment_failure_threshold", 3)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());
        headers.add("Prefer", "return=minimal");

        HttpEntity<PaypalPlanRequest> request = new HttpEntity<>(plan, headers);

        ResponseEntity<IdDTO> response = restTemplate.postForEntity(PLANS_URL, request, IdDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getId();
        } else {
            throw new SubscriptionException("Failed to create plan");
        }
    }

    public String createSubscription(String planId, Long transactionId) {
        String callbackUrl = PaymentService.buildCallbackUrl(transactionId);

        PaypalSubscriptionRequest subscription = PaypalSubscriptionRequest.builder()
                .plan_id(planId)
                .application_context(
                        ApplicationContextDTO.builder()
                                .shipping_preference("NO_SHIPPING")
                                .user_action("SUBSCRIBE_NOW")
                                .return_url(callbackUrl)
                                .cancel_url(callbackUrl)
                                .build()
                )
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());
        headers.add("Prefer", "return=minimal");

        HttpEntity<PaypalSubscriptionRequest> request = new HttpEntity<>(subscription, headers);

        ResponseEntity<LinksDTO> response = restTemplate.postForEntity(SUBSCRIPTIONS_URL, request, LinksDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getLinks().stream()
                    .filter(l -> l.getRel().equals("approve"))
                    .findAny()
                    .orElseThrow(() -> new SubscriptionException("Failed to create subscription"))
                    .getHref();
        } else {
            throw new SubscriptionException("Failed to create subscription");
        }
    }

    public Boolean isSubscriptionActive(String subscriptionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());

        ResponseEntity<SubscriptionDTO> response = restTemplate.exchange(String.format(GET_SUBSCRIPTION_URL, subscriptionId),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                SubscriptionDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getStatus().equals("ACTIVE");
        } else {
            throw new SubscriptionException("Failed to get subscription");
        }
    }
}
