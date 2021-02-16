package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MembershipTransaction extends Transaction {
    @Column
    private Long months;

    @Builder(builderMethodName = "membershipBuilder")
    public MembershipTransaction(Long id, Double total, Boolean done, TransactionStatus status, Date createdOn, Date initializedOn, Date completedOn, String paidWith, Invoice invoice, Long months) {
        super(id, total, done, status, createdOn, initializedOn, completedOn, paidWith, invoice);
        this.months = months;
    }
}
