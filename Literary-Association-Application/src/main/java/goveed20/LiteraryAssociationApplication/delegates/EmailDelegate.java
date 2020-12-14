package goveed20.LiteraryAssociationApplication.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class EmailDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String email = (String)delegateExecution.getVariable("email");
        System.out.println(email);
    }
}
