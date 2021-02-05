package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EditorReplacementDelegate implements JavaDelegate {

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        delegateExecution.setVariable("temp_editor", data.get("editor_replacement"));

        notificationService.sendSuccessNotification("Editor successfully replaced");
    }
}
