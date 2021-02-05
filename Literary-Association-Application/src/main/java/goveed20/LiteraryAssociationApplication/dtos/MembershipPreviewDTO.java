package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipPreviewDTO {
    private Long transactionId;
    private Double price;
    private String retailer;
}
