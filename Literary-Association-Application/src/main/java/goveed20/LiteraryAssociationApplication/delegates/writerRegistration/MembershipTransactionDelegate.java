package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class MembershipTransactionDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("CREATE MEMBERSHIP");
    }
}
