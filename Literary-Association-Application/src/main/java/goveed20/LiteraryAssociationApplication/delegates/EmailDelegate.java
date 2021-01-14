package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailDelegate implements JavaDelegate {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String email = (String) delegateExecution.getVariable("email");
        String userRole = (String) delegateExecution.getVariable("userRole");
        BaseUser user = userRole.equalsIgnoreCase("reader") ? readerRepository.findByEmail(email)
                : writerRepository.findByEmail(email);

        String text = String.format("Dear %s %s,%nTo confirm your account please click here: %n" +
                "http://localhost:9090/api/register/verification/" + delegateExecution
                .getProcessInstanceId() + "?token=" + verificationTokenRepository.findByUser(user)
                .getDisposableHash(), user.getName(), user.getSurname());

        emailService.sendEmail(user.getEmail(), "Complete registration", text);
    }
}
