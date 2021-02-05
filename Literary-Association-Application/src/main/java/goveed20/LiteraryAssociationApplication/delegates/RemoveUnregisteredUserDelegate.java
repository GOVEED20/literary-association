package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveUnregisteredUserDelegate implements JavaDelegate {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = String.valueOf(delegateExecution.getVariable("user"));
        baseUserRepository.deleteByUsername(username);
    }
}
