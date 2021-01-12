package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class PlagiarismDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private Boolean plagiarism;

    @ManyToOne(optional = false)
    private PlagiarismComplaint plagiarismComplaint;

    @ManyToOne(optional = false)
    private BaseUser user;
}
