package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class BetaReaderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(targetClass = Genre.class)
    @JoinTable(name = "genres", joinColumns = @JoinColumn(name = "beta_id"))
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Genre> betaGenres;

    @Column(nullable = false)
    private Integer penaltyPoints = 0;

    @OneToOne(optional = false)
    private Reader reader;

    @ManyToMany
    private Set<WorkingPaper> betaReaderPapers;
}
