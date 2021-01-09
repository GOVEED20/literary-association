package goveed20.CardPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomersBankResponseDTO {
    private String acquirerOrderID;
    private String acquirerTimestamp;
    private String issuerOrderID;
    private String issuerTimestamp;
    private String errorMessage;
}
