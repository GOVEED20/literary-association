package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EditorReplacementDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        delegateExecution.setVariable("temp_editor", data.get("editor_replacement"));
        Map<String, String> chosenEditors = (Map<String, String>) delegateExecution.getVariable("chosen_editors");
        chosenEditors.remove((String) delegateExecution.getVariable("current_editor"));
        chosenEditors.put((String) data.get("editor_replacement"), "");
        delegateExecution.setVariable("chosen_editors", chosenEditors);
    }
}
