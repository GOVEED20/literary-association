package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipInvoiceDTO {
    @NotBlank
    private String paymentMethod;

    @NotNull
    private Long membershipTransactionId;

    @NotNull
    private Boolean subscription;
}
