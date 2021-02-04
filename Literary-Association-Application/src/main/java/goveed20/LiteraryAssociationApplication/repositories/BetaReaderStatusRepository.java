package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.BetaReaderStatus;
import goveed20.LiteraryAssociationApplication.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BetaReaderStatusRepository extends JpaRepository<BetaReaderStatus, Long> {

    List<BetaReaderStatus> findByBetaGenresContaining(Genre genre);
}
