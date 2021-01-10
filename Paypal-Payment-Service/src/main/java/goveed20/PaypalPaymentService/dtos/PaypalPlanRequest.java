package goveed20.PaypalPaymentService.dtos;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaypalPlanRequest {
    private String product_id;
    private String name;

    @Singular("billing_cycle")
    private List<BillingCycleDTO> billing_cycles;

    @Singular("payment_preference")
    private Map<String, Object> payment_preferences;
}
