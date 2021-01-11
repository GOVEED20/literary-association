package goveed20.PaymentConcentrator.dtos;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentServiceData {
    private String serviceName;
    private List<RegistrationFieldForm> data;
}
