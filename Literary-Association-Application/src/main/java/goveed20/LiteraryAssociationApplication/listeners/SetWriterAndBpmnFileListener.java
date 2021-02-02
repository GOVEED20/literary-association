package goveed20.LiteraryAssociationApplication.listeners;


import goveed20.LiteraryAssociationApplication.model.BaseUser;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SetWriterAndBpmnFileListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        BaseUser writer = (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        delegateExecution.setVariable("writer", writer.getUsername());
        delegateExecution.setVariable("bpmnFile", "src/main/resources/book_publishing.bpmn");
    }
}
