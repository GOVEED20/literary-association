package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDataPayload extends BasePayload {
    @NotBlank
    private UUID transactionID;

    @Builder(builderMethodName = "childBuilder")
    public TransactionDataPayload(HashMap<String, String> paymentFields, UUID transactionId) {
        super(paymentFields);
        this.transactionID = transactionId;
    }
}
