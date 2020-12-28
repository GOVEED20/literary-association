package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitializationPaymentPayload {
    @NotEmpty
    private HashMap<String, String> paymentFields;

    @NotNull
    private Long transactionId;

    @NotNull
    @Min(0)
    private Double amount;

    private String successURL;

    private String failedURL;

    private String errorURL;
}
