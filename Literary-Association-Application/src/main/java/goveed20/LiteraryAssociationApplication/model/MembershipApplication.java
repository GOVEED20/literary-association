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
public class MembershipApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer iteration;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicationPaper> applicationPapers;
}
