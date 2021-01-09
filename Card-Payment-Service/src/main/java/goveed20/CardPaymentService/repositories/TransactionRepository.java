package goveed20.CardPaymentService.repositories;

import goveed20.CardPaymentService.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionID(String transactionID);
}
