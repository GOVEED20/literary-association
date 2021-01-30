package goveed20.PaymentConcentrator.repositories;

import goveed20.PaymentConcentrator.model.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    Optional<Retailer> findByName(String name);

    Optional<Retailer> findByRegistrationToken(String registrationToken);
}
