package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Boolean done;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date initializedOn;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;

    @Column
    private String paidWith;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Invoice invoice;
}
