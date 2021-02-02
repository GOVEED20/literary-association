package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class UploadToPlagiarismSystemDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Plagiarism check");
    }
}
