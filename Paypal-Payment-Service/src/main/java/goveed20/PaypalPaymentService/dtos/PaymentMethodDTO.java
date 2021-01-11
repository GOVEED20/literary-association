package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaymentMethodDTO {
    private String payer_selected;
    private String payee_preferred;
}
