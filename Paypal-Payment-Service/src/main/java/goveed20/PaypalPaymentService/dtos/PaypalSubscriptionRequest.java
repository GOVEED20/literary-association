package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaypalSubscriptionRequest {
    private String plan_id;
    private ApplicationContextDTO application_context;
}
