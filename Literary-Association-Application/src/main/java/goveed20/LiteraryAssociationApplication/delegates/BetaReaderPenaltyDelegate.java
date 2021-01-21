package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class BetaReaderPenaltyDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Reader reader = readerRepository.findByUsername((String) delegateExecution
                .getVariable("current_beta_reader"));
        if (reader != null) {
            Integer penaltyPoints = reader.getBetaReaderStatus().getPenaltyPoints();
            penaltyPoints = ++penaltyPoints;
            if (penaltyPoints == 5) {
                String text = String.format("Dear %s %s,\nYou have collected 5 penalty points and because of that " +
                                "you lose your beta reader status.",
                        reader.getName(),
                        reader.getSurname());
                emailService.sendEmail(reader.getEmail(), "Beta reader status lost", text);
                reader.setBetaReader(false);
                // TODO: What to do with BetaReaderStatus object (physically delete, assign status, ...)
                readerRepository.save(reader);
            }
            else {
                reader.getBetaReaderStatus().setPenaltyPoints(penaltyPoints);
                readerRepository.save(reader);
            }
        }
    }
}
