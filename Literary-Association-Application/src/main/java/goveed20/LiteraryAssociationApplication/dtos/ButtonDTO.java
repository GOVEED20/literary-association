package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ButtonDTO {
    private String id;
    private String label;
    private String title;
    private String downloadURL;
}
