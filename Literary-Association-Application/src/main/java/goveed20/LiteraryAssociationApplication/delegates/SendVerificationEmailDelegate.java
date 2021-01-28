package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendVerificationEmailDelegate implements JavaDelegate {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String email = (String) delegateExecution.getVariable("email");
        Optional<BaseUser> userOptional = baseUserRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            BaseUser user = userOptional.get();

            String text = String.format("Dear %s %s,\nTo confirm your account please click here: \n" +
                    "http://localhost:9090/api/register/verification/" + delegateExecution
                    .getProcessInstanceId() + "?token=" + verificationTokenRepository.findByUser(user)
                    .getDisposableHash(), user.getName(), user.getSurname());

            emailService.sendEmail(user.getEmail(), "Complete registration", text);
        }
    }
}
