package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByDisposableHash(String disposableHash);

    VerificationToken findByUser(BaseUser user);
}
