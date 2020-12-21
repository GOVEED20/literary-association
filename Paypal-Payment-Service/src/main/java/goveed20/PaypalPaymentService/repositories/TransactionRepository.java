package goveed20.PaypalPaymentService.repositories;

import goveed20.PaypalPaymentService.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, BigInteger> {
    @Query("{ 'payment._id': ?0 }")
    Optional<Transaction> findTransactionByPayment(String paymentId);
}
