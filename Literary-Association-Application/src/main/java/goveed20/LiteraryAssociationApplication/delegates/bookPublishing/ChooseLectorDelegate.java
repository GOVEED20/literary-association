package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChooseLectorDelegate implements JavaDelegate {

    @Autowired
    private BaseUserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        ArrayList<BaseUser> lectors = (ArrayList<BaseUser>) userRepository.findAllByRole(UserRole.LECTOR);
        BaseUser lector = lectors.get((int) (Math.random() * lectors.size()));

        delegateExecution.setVariable("lector", lector.getUsername());

        String text = String
                .format("Dear %s %s,\nYou have been chosen for reviewing new working paper with title %s", lector
                                .getName(),
                        lector.getSurname(), delegateExecution.getVariable("working_paper"));
        emailService.sendEmail(lector.getEmail(), "Reviewing new working paper", text);
    }
}
