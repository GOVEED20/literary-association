package goveed20.CardPaymentService.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CardholderDataDTO {
    private String PAN;
    private String securityCode;
    private String cardHolderName;
    private Date expiryDate;
}
