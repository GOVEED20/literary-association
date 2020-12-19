package goveed20.PaymentConcentrator.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.UUID;

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

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;

    @Column(unique = true)
    private UUID transactionId;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date initializedOn;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date completedOn;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]+-service$")
    private String paidWith;
}
