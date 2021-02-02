package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.BetaReaderStatus;
import goveed20.LiteraryAssociationApplication.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BetaReaderStatusRepository extends JpaRepository<BetaReaderStatus, Long> {

    @Query("SELECT b FROM BetaReaderStatus b WHERE :genre IN (b.betaGenres)")
    List<BetaReaderStatus> findByGenre(@Param("genre") Genre genre);
}
