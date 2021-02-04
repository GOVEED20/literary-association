package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdditionalContentDTO {
    private boolean isComment;
    private Object content;
}
