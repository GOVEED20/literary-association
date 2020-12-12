package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayload extends BasePayload {

    @NotNull
    private TransactionStatus transactionStatus;

    @NotBlank
    private String transactionID;
}
