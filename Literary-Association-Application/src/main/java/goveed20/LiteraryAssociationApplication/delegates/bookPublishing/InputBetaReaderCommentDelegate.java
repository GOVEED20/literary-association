package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.ApplicationPaper;
import goveed20.LiteraryAssociationApplication.model.Comment;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import goveed20.LiteraryAssociationApplication.repositories.CommentRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class InputBetaReaderCommentDelegate implements JavaDelegate {

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        String writerUsername = (String) delegateExecution.getVariable("writer");
        Writer writer = writerRepository.findByUsername(writerUsername).get();
        String workingPaperTitle = (String) delegateExecution.getVariable("working_paper");

        WorkingPaper workingPaper = workingPaperRepository.findByTitle(workingPaperTitle);
        Set<ApplicationPaper> paperSet = new HashSet<>();
        paperSet.add(workingPaper);
        Comment comment = Comment.builder()
                .applicationPapers(paperSet)
                .content((String) data.get("beta_reader_comment"))
                .type(CommentType.BETA_READER_COMMENT)
                .user(writer)
                .build();
        commentRepository.save(comment);

        notificationService.sendSuccessNotification("Comment successfully submitted");
    }
}
