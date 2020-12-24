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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentType type;

    @ManyToOne(optional = false)
    private BaseUser user;

    @OneToMany
    private Set<ApplicationPaper> applicationPapers;
}
