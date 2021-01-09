package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePayload {

    @NotNull
    private TransactionStatus transactionStatus;

    @NotBlank
    private Long transactionID;

    private Map<String, String> paymentData;
}
