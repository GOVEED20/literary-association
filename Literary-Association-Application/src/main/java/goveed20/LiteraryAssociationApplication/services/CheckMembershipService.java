package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.configs.YAMLConfig;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.InvoiceRepository;
import goveed20.LiteraryAssociationApplication.repositories.MembershipTransactionRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class CheckMembershipService {
    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private MembershipTransactionRepository membershipTransactionRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private YAMLConfig config;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @SneakyThrows
    @Scheduled(cron = "0 0 0 * * *")
    private void checkIfMembershipIsExpired() {
        writerRepository.findByMembershipApprovedIsTrue().forEach(writer -> {
            if (isMembershipExpired(writer)) {
                Retailer la = retailerRepository.findByName(config.getName())
                        .orElseThrow(() -> new NotFoundException("Literary association retailer not found"));

                Set<InvoiceItem> invoiceItems = new HashSet<>();
                InvoiceItem membership = InvoiceItem.builder()
                        .price(10.0)
                        .name("Membership")
                        .build();

                invoiceItems.add(membership);

                Invoice invoice = Invoice.builder()
                        .invoiceItems(invoiceItems)
                        .retailer(la)
                        .build();

                invoiceRepository.save(invoice);

                MembershipTransaction transaction = MembershipTransaction.membershipBuilder()
                        .createdOn(new Date())
                        .status(TransactionStatus.CREATED)
                        .done(false)
                        .invoice(invoice)
                        .total(10.0)
                        .build();

                membershipTransactionRepository.save(transaction);

                writer.getTransactions().add(transaction);
                writerRepository.save(writer);
            }
        });
    }

    @SneakyThrows
    private boolean isMembershipExpired(Writer writer) {
        MembershipTransaction lastMembership = writer.getTransactions().stream()
                .filter(t -> t instanceof MembershipTransaction)
                .reduce((t1, t2) -> t1.getCreatedOn().compareTo(t2.getCreatedOn()) > 0 ? t1 : t2)
                .map(t -> (MembershipTransaction) t)
                .get();

        if (lastMembership.getDone() && !lastMembership.getMonths().equals(0L)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastMembership.getCompletedOn());
            cal.add(Calendar.MONTH, 1);

            return cal.getTime().compareTo(new Date()) <= 0;
        }
        return false;
    }
}
