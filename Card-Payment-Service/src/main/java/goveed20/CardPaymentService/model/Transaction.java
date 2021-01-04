package goveed20.CardPaymentService.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Transaction {

    @Column(unique = true, nullable = false)
    private String transactionID;

    @Column(unique = true, nullable = false)
    private String paymentID;

    @Column
    private String merchantID;

    @Column(unique = true, nullable = false)
    private String merchantOrderID;

    @Column(nullable = false)
    private String merchantTimestamp;

    @Column(nullable = false)
    private Double amount;

    @Column(unique = true)
    private String acquirerOrderID;

    @Column
    private String acquirerTimestamp;

    @Column(unique = true)
    private String issuerOrderID;

    @Column
    private String issuerTimestamp;
}
