package goveed20.CardPaymentService.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "((0[1-9])|(1[0-2]))/[0-9]{4}")
    private String expiryDate;
}
