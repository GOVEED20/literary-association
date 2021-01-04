package goveed20.PaymentConcentrator.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

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
    private Long transactionId;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date initializedOn;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date completedOn;

    @Column(nullable = false)
    private Double amount;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<PaymentData> paymentData;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]+-service$")
    private String paidWith;

    @Column(nullable = false)
    private String successURL;

    @Column(nullable = false)
    private String failedURL;

    @Column(nullable = false)
    private String errorURL;
}
