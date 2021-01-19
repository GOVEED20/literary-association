package goveed20.LiteraryAssociationApplication.delegates;

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
public class ChooseEditorDelegate implements JavaDelegate {

    @Autowired
    private BaseUserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        ArrayList<BaseUser> editors = (ArrayList<BaseUser>) userRepository.findAllByRole(UserRole.EDITOR);
        BaseUser editor = editors.get((int)(Math.random() * editors.size()));

        delegateExecution.setVariable("editor", editor.getUsername());

        String text = String.format("Dear %s %s,\nNew book request is received from %s", editor.getName(),
                editor.getSurname(), delegateExecution.getVariable("writer"));
        emailService.sendEmail(editor.getEmail(), "New book request", text);
    }
}
