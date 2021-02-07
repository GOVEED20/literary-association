package goveed20.CardPaymentService.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column
    private Boolean completed;
}
