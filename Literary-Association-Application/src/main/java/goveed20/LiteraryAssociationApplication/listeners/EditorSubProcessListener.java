package goveed20.LiteraryAssociationApplication.listeners;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class EditorSubProcessListener implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String editor = (String) delegateExecution.getVariable("current_editor");
        delegateExecution.setVariable("temp_editor", editor);
        delegateExecution.setVariable("bpmnFile", "editor_comparison_notes");
    }
}
