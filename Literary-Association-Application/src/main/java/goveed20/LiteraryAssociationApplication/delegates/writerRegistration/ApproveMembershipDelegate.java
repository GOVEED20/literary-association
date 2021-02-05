package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApproveMembershipDelegate implements ExecutionListener {
    @Autowired
    private WriterRepository writerRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String username = String.valueOf(delegateExecution.getVariable("user"));

        Writer writer = writerRepository.findByUsername(username).get();
        writer.setMembershipApproved(true);
        writerRepository.save(writer);
    }
}
