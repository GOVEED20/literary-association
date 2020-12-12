package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitializationPaymentPayload extends BasePayload {

    @NotNull
    @Min(0)
    private Double amount;

    @NotBlank
    private String successURL;

    @NotBlank
    private String failedURL;

    @NotBlank
    private String errorURL;
}
