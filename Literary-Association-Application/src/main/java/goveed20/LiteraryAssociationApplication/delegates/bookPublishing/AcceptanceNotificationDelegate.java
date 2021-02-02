package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcceptanceNotificationDelegate implements JavaDelegate {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Writer writer = writerRepository.findByUsername((String) delegateExecution
                .getVariable("writer")).get();
        String text = String.format("Dear %s %s,%nYour working paper is accepted", writer.getName(),
                writer.getSurname());

        emailService.sendEmail(writer.getEmail(), "Working paper acceptance", text);
    }
}
