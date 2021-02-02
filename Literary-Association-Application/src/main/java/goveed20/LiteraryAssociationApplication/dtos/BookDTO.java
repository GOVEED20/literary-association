package goveed20.LiteraryAssociationApplication.dtos;

import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private String title;
    private GenreEnum genreEnum;
    private String synopsis;
    private String ISBN;
    private String publisher;
    private Integer year;
    private String place;
    private Double price;
    // add authors later
}
