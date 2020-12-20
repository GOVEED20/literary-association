package goveed20.PaypalPaymentService.repositories;

import goveed20.PaypalPaymentService.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface TransactionRepository extends MongoRepository<Transaction, BigInteger> {
}
