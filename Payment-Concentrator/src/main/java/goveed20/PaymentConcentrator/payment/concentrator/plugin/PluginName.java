package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PluginName {
    @Pattern(regexp = "^[A-Za-z]+-service$")
    private String name;
}
