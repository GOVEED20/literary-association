package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OptionDTO {
    private String name;
    private Object value;
}
