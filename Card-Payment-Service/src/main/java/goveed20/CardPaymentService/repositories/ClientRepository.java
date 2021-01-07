package goveed20.CardPaymentService.repositories;

import goveed20.CardPaymentService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
