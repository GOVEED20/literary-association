package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import goveed20.LiteraryAssociationApplication.model.ApplicationPaper;
import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.Comment;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import goveed20.LiteraryAssociationApplication.repositories.CommentRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class InputComparisonNoteDelegate implements JavaDelegate {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        WorkingPaper workingPaper = workingPaperRepository
                .findByTitle((String) delegateExecution.getVariable("my_book"));
        Set<ApplicationPaper> paperSet = new HashSet<>();
        paperSet.add(workingPaper);

        Comment comment = Comment.builder()
                .applicationPapers(paperSet)
                .content((String) data.get("note"))
                .type(CommentType.EDITOR_COMMENT)
                .user(((Book) workingPaper).getWriter())
                .build();
        commentRepository.save(comment);

        notificationService.sendSuccessNotification("Plagiarism note has been successfully sent");
    }
}
