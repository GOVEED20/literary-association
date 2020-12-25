package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
