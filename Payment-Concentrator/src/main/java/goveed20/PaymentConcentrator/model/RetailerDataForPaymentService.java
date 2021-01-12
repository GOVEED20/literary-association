package goveed20.PaymentConcentrator.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class RetailerDataForPaymentService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]+-service$")
    private String paymentService;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<PaymentData> paymentData = new HashSet<>();

    @ManyToOne
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Retailer retailer;
}
