package goveed20.PaypalPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApplicationContextDTO {
    private String shipping_preference;
    private String user_action;
    private String return_url;
    private String cancel_url;
}
