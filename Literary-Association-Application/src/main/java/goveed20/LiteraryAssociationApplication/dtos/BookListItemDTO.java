package goveed20.LiteraryAssociationApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookListItemDTO {
    Long id;
    String title;
    String publisher;
    String ISBN;
    Integer year;
}
