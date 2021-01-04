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
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "([0-9]{4}-){3}[0-9]{4}")
    private String PAN;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Card card;

    @Column(nullable = false)
    private Double balance;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OnlinePaymentData onlinePaymentData;
}
