package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MarkDownLexicographicMistakesDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        delegateExecution.setVariable("correct_mistakes", !data.get("mistake_comment").equals(""));
        delegateExecution.setVariable("lector_comment", data.get("mistake_comment"));
    }
}
