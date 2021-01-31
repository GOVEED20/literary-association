package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlagiarismCommentInputDelegate implements JavaDelegate {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private EmailService emailService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        Writer writer = writerRepository.findByUsername((String) delegateExecution
                .getVariable("writer")).get();
        String text = String.format("Dear %s %s,\nYour working paper is rejected as a plagiarism." +
                        writer.getName(), writer.getSurname(), data.get("plagiarism_reject_comment"));

        emailService.sendEmail(writer.getEmail(), "Working paper rejection", text);
    }
}
