package goveed20.PaymentConcentrator.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<RetailerDataForPaymentService> retailerDataForPaymentServices = new HashSet<>();

    @Builder.Default
    @OneToMany
    private Set<Transaction> transactions = new HashSet<>();
}
