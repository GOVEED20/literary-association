package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayload extends BasePayload {

    @NotNull
    private TransactionStatus transactionStatus;

    @NotBlank
    private UUID transactionID;

    @Builder(builderMethodName = "childBuilder")
    public ResponsePayload(HashMap<String, String> paymentFields, TransactionStatus transactionStatus, UUID transactionID) {
        super(paymentFields);
        this.transactionStatus = transactionStatus;
        this.transactionID = transactionID;
    }
}
