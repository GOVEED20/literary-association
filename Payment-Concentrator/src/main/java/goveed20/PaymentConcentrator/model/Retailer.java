package goveed20.PaymentConcentrator.model;

import lombok.*;

import javax.persistence.*;
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

    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<RetailerDataForPaymentService> retailerDataForPaymentServices;

    @OneToMany
    private Set<Transaction> transactions;
}
