package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FrequencyDTO {
    private String interval_unit;
    private Integer interval_count;
}
