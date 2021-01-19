package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByUsername(String username);
    List<BaseUser> findAllByRole(UserRole role);
}
