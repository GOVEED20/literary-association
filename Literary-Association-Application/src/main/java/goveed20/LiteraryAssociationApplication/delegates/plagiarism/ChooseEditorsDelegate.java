package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ChooseEditorsDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        Set<String> editorsSet = UtilService.parseEditors((String) data.get("editors"));
        if (editorsSet.size() < 2) {
            throw new BpmnError("You must chose at least 2 editors");
        }
        Map<String,String> chosenEditors = new HashMap<>();
        editorsSet.forEach(e -> chosenEditors.put(e, ""));

        delegateExecution.setVariable("chosen_editors", chosenEditors);
    }
}
