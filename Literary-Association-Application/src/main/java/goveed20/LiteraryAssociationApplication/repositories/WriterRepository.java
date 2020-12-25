package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<Writer, Long> {

    Writer findByEmail(String email);
}
