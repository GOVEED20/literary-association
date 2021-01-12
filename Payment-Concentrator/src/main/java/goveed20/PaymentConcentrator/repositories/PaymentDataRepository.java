package goveed20.PaymentConcentrator.repositories;

import goveed20.PaymentConcentrator.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {
}
