package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InputComparisonNoteDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        Map<String, String> chosenEditors = (Map<String, String>) delegateExecution.getVariable("chosen_editors");
        chosenEditors.replace((String) delegateExecution.getVariable("temp_editor"), (String) data.get("note"));
        delegateExecution.setVariable("chosen_editors", chosenEditors);
    }
}
