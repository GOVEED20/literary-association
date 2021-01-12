package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationFieldForm {
    private String name;
    private String value;
    private Boolean encrypted;
}
