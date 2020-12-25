package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private GenreEnum genre;
}
