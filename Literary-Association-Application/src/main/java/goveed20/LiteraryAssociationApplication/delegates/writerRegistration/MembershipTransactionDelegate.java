package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.configs.YAMLConfig;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.InvoiceRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.repositories.TransactionRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class MembershipTransactionDelegate implements JavaDelegate {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = String.valueOf(delegateExecution.getVariable("user"));
        Writer writer = writerRepository.findByUsername(username).get();
        Retailer la = retailerRepository.findByName(yamlConfig.getName()).get();

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

        transactionRepository.save(transaction);

        writer.getTransactions().add(transaction);
        writerRepository.save(writer);
    }
}
