package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpirationNotificationDelegate implements JavaDelegate {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Writer writer = writerRepository.findByUsername((String) delegateExecution
                .getVariable("writer")).get();
        String text = String.format("Dear %s %s,%nTime for submitting full working paper has expired", writer.getName(),
                writer.getSurname());

        emailService.sendEmail(writer.getEmail(), "Submitting period expired", text);
    }
}
