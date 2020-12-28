package goveed20.LiteraryAssociationApplication.dtos;

import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OptionDTO {
    private String name;
    private GenreEnum value;
}
