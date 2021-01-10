package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaypalProductRequest {
    private String name;
    private String type;
    private String category;
}
