package goveed20.CardPaymentService.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String securityCode;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryDate;
}
