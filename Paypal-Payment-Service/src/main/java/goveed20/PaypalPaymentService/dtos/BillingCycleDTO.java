package goveed20.PaypalPaymentService.dtos;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BillingCycleDTO {
    private FrequencyDTO frequency;
    private String tenure_type;
    private Integer sequence;
    private Integer total_cycles;

    @Singular("pricing_scheme")
    private Map<String, PriceDTO> pricing_scheme;

}
