package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDataPayload extends BasePayload {
    @NotBlank
    private String transactionID; // Payment concentrator transaction ID
}
