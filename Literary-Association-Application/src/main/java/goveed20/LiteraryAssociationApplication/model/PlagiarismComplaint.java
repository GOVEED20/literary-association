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
public class PlagiarismComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    private Book original;

    @OneToOne(optional = false)
    private Book potentialPlagiarism;

    @OneToMany
    private Set<PlagiarismDecision> plagiarismDecisions;
}
