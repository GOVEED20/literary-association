package goveed20.PaymentConcentrator.repositories;

import goveed20.PaymentConcentrator.model.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {
}
