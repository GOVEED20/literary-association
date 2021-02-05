package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WriterRepository extends JpaRepository<Writer, Long> {
    Writer findByEmail(String email);

    Optional<Writer> findByUsername(String username);

    List<Writer> findByMembershipApprovedIsTrue();
}
