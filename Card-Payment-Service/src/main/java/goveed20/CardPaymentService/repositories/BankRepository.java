package goveed20.CardPaymentService.repositories;

import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
