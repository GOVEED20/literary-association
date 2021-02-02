package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EditorRequestChangesDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        delegateExecution.setVariable("editor_request_changes", data.get("editor_request_changes_option").equals("Request changes"));
    }
}
