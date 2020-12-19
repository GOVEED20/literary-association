package goveed20.PaymentConcentrator.repositories;

import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerDataForPaymentServiceRepository extends JpaRepository<RetailerDataForPaymentService, Long> {
}
