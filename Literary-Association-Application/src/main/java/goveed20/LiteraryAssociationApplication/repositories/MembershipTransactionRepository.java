package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.MembershipTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipTransactionRepository extends JpaRepository<MembershipTransaction, Long> {
}
