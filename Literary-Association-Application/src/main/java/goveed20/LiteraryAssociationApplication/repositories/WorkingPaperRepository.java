package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingPaperRepository extends JpaRepository<WorkingPaper, Long> {
    WorkingPaper findByTitle(String workingPaperTitle);
}
