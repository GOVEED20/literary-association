package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ServiceFieldsCheck {
    private String validationMessage;
    private List<RegistrationFieldForm> additionalFields;
}
