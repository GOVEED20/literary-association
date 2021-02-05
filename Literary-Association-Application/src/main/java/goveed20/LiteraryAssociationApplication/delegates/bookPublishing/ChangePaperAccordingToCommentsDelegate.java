package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.services.BookService;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Map;

@Service
public class ChangePaperAccordingToCommentsDelegate implements JavaDelegate {

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        WorkingPaper paper;
        try {
            paper = bookService.submitPaper(delegateExecution.getProcessInstanceId(), (String) data.get("changed_paper"));
        } catch (BusinessProcessException | EntityNotFoundException e) {
            throw notificationService.sendErrorNotification(e.getMessage());
        } catch (IOException e) {
            throw notificationService.sendErrorNotification("Working paper not found");
        }

        workingPaperRepository.save(paper);

        notificationService.sendSuccessNotification("File successfully uploaded");
    }
}
