package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SubmitWorkingPaperTemplateDelegate implements JavaDelegate {

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        String title = (String) data.get("title");
        if (workingPaperRepository.findByTitle(title) != null) {
            throw new BpmnError("Book with given title already exists");
        }

        delegateExecution.setVariable("working_paper", title);

        WorkingPaper workingPaper = WorkingPaper.workingPaperBuilder().title(title)
                .synopsis((String) data.get("synopsis")).genre(Genre.builder()
                        .genre(GenreEnum.valueOf((String) data.get("genre"))).build()).build();
        workingPaperRepository.save(workingPaper);
    }
}
