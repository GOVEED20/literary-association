package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.User;
import goveed20.LiteraryAssociationApplication.repositories.UserRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailDelegate implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String email = (String)delegateExecution.getVariable("email");
        User user = userRepository.findByEmail(email);
        emailService.sendVerificationEmail(user, verificationTokenRepository.findByUser(user),
                delegateExecution.getProcessInstanceId());
    }
}
