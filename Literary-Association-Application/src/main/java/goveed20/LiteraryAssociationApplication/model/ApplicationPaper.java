package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ApplicationPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(nullable = false)
    protected String file;
}
