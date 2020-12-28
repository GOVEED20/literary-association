package goveed20.LiteraryAssociationApplication.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date initializedOn;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<InvoiceItem> invoiceItems;

    @ManyToOne
    private Retailer retailer;

    @OneToOne(optional = false)
    private Transaction transaction;
}
