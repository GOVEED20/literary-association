package goveed20.PaypalPaymentService.services;

import com.paypal.base.rest.APIContext;
import goveed20.PaypalPaymentService.dtos.*;
import goveed20.PaypalPaymentService.exceptions.SubscriptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@SuppressWarnings("deprecation")
@Service
public class PaypalSubscriptionsService {
    @Autowired
    private APIContext apiContext;

    private final RestTemplate restTemplate = new RestTemplate();

    static final String PRODUCTS_URL = "https://api-m.sandbox.paypal.com/v1/catalogs/products";
    static final String PLANS_URL = "https://api-m.sandbox.paypal.com/v1/billing/plans";
    static final String SUBSCRIPTIONS_URL = "https://api-m.sandbox.paypal.com/v1/billing/subscriptions";

    public String createProduct() {
        PaypalProductRequest product = PaypalProductRequest.builder()
                .name("Subscription")
                .description("Literary association subscription")
                .type("SERVICE")
                .category("MEMBERSHIP_CLUBS_AND_ORGANIZATIONS")
                .image_url("https://example.com/streaming.jpg")
                .home_url("https://example.com/home")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());

        HttpEntity<PaypalProductRequest> request = new HttpEntity<>(product, headers);

        ResponseEntity<IdDTO> response = restTemplate.postForEntity(PRODUCTS_URL, request, IdDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getId();
        } else {
            throw new SubscriptionException("Failed to create product");
        }
    }

    public String createPlan(String productId) {
        PaypalPlanRequest plan = PaypalPlanRequest.builder()
                .product_id(productId)
                .name(String.format("Plan for %s", productId))
                .description("Plan")
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
                                                .value("10")
                                                .currency_code("USD")
                                                .build()
                                )
                                .build()
                )
                .payment_preference("auto_bill_outstanding", true)
                .payment_preference("payment_failure_threshold", 3)
                .taxes(TaxDTO.builder()
                        .percentage("0")
                        .inclusive(false)
                        .build()
                )
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());
        headers.add("Prefer", "return=representation");

        HttpEntity<PaypalPlanRequest> request = new HttpEntity<>(plan, headers);

        ResponseEntity<IdDTO> response = restTemplate.postForEntity(PLANS_URL, request, IdDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getId();
        } else {
            throw new SubscriptionException("Failed to create plan");
        }
    }

    public String createSubscription(String planId) {
        String callbackUrl = PaymentService.buildCallbackUrl(1L);

        PaypalSubscriptionRequest subscription = PaypalSubscriptionRequest.builder()
                .plan_id(planId)
                .start_time(createSubscriptionStartDate())
                .subscriber(SubscriberDTO.builder().email_address("sb-jysh474156546@personal.example.com").build())
                .application_context(
                        ApplicationContextDTO.builder()
                                .shipping_preference("NO_SHIPPING")
                                .user_action("SUBSCRIBE_NOW")
                                .payment_method(
                                        PaymentMethodDTO.builder()
                                                .payee_preferred("IMMEDIATE_PAYMENT_REQUIRED")
                                                .payer_selected("PAYPAL")
                                                .build()
                                )
                                .build()
                )
                .return_url(callbackUrl)
                .cancel_url(callbackUrl)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", apiContext.getAccessToken());
        headers.add("Prefer", "return=representation");

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

    private String createSubscriptionStartDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(tz);

        Calendar cal = Calendar.getInstance(tz);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(cal.getTime());
    }
}
