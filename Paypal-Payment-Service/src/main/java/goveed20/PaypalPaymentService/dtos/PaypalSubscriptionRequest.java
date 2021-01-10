package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaypalSubscriptionRequest {
    private String plan_id;
    private String start_time;
    private SubscriberDTO subscriber;
    private ApplicationContextDTO application_context;
    private String return_url;
    private String cancel_url;
}
