package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationField {
    private String name;
    private RegistrationFieldType type;
    private Map<String, Object> validationConstraints;
    private Boolean encrypted;
}
