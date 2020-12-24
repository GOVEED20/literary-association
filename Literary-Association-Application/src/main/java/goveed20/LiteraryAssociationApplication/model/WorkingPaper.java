package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkingPaper extends ApplicationPaper {
    @Column(nullable = false)
    protected String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Genre genre;

    @Column(unique = false)
    protected String synopsis;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkingPaperStatus status;

    @Builder(builderMethodName = "workingPaperBuilder", toBuilder = true)
    public WorkingPaper(Long id, String file, String title, Genre genre, String synopsis, WorkingPaperStatus status) {
        super(id, file);
        this.title = title;
        this.genre = genre;
        this.synopsis = synopsis;
        this.status = status;
    }
}
