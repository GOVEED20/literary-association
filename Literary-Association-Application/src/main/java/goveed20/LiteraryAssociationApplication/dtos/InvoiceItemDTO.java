package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemDTO {
    @NotNull
    private Long id;

    @NotNull
    @Min(1)
    private Integer quantity;
}
