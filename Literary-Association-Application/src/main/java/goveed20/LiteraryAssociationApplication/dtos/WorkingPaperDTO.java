package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WorkingPaperDTO {
    private String title;
    private String synopsis;
    private String genre;
    private String author;
}
