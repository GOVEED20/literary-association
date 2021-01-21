package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyWriterAboutSuggestionsExpirationDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private WriterRepository writerRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Writer writer = writerRepository.findByUsername((String) delegateExecution
                .getVariable("writer")).get();
        String text = String.format("Dear %s %s,\nYour working paper is rejected because your " +
                        "expiration date for changing paper according to suggestions has expired.",
                writer.getName(),
                writer.getSurname());

        emailService.sendEmail(writer.getEmail(), "Working paper rejection", text);
    }
}
