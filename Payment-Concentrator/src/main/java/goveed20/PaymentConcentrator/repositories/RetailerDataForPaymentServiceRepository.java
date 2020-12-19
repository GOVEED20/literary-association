package goveed20.PaymentConcentrator.repositories;

import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RetailerDataForPaymentServiceRepository extends JpaRepository<RetailerDataForPaymentService, Long> {
    Set<RetailerDataForPaymentService> findByRetailer(Long retailerId);
}
