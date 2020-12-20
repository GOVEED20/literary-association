package goveed20.PaymentConcentrator.dtos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InitializePaymentRequest {
    @NotBlank
    private String retailer;

    @NotNull
    @Min(0)
    private Double amount;

    @NotNull
    private UUID transactionId;

    @NotBlank
    private String successURL;

    @NotBlank
    private String failedURL;

    @NotBlank
    private String errorURL;
}
