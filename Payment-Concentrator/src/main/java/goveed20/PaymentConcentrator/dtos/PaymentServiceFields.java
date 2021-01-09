package goveed20.PaymentConcentrator.dtos;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentServiceFields {
    private String serviceName;
    private Set<RegistrationFieldForm> data;
}
