package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFieldsDTO {
    @NotNull private Boolean subscription;
    private String name;
}
