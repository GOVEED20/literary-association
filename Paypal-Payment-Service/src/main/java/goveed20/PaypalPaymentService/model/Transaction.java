package goveed20.PaypalPaymentService.model;

import com.paypal.api.payments.Payment;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "transactions")
public class Transaction {
    @Id
    private BigInteger id;

    private UUID transactionId; // from PC

    private Payment payment;
}
