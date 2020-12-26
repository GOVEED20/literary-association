package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDataPayload {
    private HashMap<String, String> paymentFields;

    @NotBlank
    private UUID transactionID;
}
