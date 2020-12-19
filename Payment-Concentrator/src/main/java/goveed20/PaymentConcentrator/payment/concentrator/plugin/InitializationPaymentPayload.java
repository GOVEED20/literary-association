package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitializationPaymentPayload extends BasePayload {

    @NotNull
    private UUID transactionId;

    @NotNull
    @Min(0)
    private Double amount;

    @NotBlank
    private String successURL;

    @NotBlank
    private String failedURL;

    @NotBlank
    private String errorURL;

    @Builder(builderMethodName = "childBuilder")
    public InitializationPaymentPayload(HashMap<String, String> paymentFields, UUID transactionId, Double amount,
                                        String successURL, String failedURL, String errorURL) {
        super(paymentFields);
        this.transactionId = transactionId;
        this.amount = amount;
        this.successURL = successURL;
        this.errorURL = errorURL;
        this.failedURL = failedURL;
    }
}
