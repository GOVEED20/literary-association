package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PriceDTO {
    private String value;
    private String currency_code;
}
