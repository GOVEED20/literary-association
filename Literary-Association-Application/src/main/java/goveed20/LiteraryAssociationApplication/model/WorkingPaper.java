package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkingPaper extends ApplicationPaper {
    @Column(nullable = false, unique = true)
    protected String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre", referencedColumnName = "id")
    protected Genre genre;

    @Column
    protected String synopsis;

    @Column
    @Enumerated(EnumType.STRING)
    private WorkingPaperStatus status;

    @Builder(builderMethodName = "workingPaperBuilder")
    public WorkingPaper(Long id, String file, String title, Genre genre, String synopsis, WorkingPaperStatus status) {
        super(id, file);
        this.title = title;
        this.genre = genre;
        this.synopsis = synopsis;
        this.status = status;
    }
}
