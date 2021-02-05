package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.MembershipPreviewDTO;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.model.MembershipTransaction;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
    @Autowired
    private WriterRepository writerRepository;

    public MembershipPreviewDTO getActiveMembership(String username) {
        return writerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("Writer with username '%s' not found", username)))
                .getTransactions().stream()
                .filter(t -> t instanceof MembershipTransaction && t.getStatus().equals(TransactionStatus.CREATED))
                .map(t -> new MembershipPreviewDTO(t.getId(), t.getTotal(), t.getInvoice().getRetailer().getName()))
                .findFirst().orElse(null);
    }
}
