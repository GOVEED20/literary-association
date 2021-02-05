package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long transactionId;
    private Double amount;
    private String successURL;
    private String failedURL;
    private String errorURL;
    private String retailer;
    private PaymentFieldsDTO paymentFields;
}
