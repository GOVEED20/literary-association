package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {
    @NotBlank
    private String retailer;

    @NotBlank
    private String paymentMethod;

    @NotNull
    private Boolean subscription;

    @NotNull
    @NotEmpty
    private List<InvoiceItemDTO> invoiceItems;
}
