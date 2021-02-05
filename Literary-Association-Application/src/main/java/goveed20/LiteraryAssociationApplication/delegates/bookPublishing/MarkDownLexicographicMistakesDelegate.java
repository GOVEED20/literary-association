package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MarkDownLexicographicMistakesDelegate implements JavaDelegate {

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        delegateExecution.setVariable("correct_mistakes", !data.get("mistake_comment").equals(""));
        delegateExecution.setVariable("lector_comment", data.get("mistake_comment"));

        notificationService.sendSuccessNotification("Comment successfully submitted");
    }
}
