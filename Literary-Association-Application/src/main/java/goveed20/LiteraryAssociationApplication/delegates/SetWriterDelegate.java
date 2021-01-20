package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;

@Service
public class SetWriterDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        BaseUser writer = (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        delegateExecution.setVariable("writer", writer.getUsername());
    }
}
