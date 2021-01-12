package goveed20.CardPaymentService.repositories;

import goveed20.CardPaymentService.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
