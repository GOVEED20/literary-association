package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.services.PdfService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SaveWritingsDelegate implements JavaDelegate {
    @Autowired
    private PdfService pdfService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        String writingsString = String.valueOf(data.get("writings"));
        String[] writings = writingsString.split(" ");

        if (writings.length < 2) {
            throw new BpmnError("At least 2 writings should be submitted");
        }

        try {
            List<String> writingsTitles = pdfService.saveBase64ToPdf(writings);
            delegateExecution.setVariable("writings_titles", writingsTitles);
        } catch (BusinessProcessException e) {
            throw new BpmnError(e.getMessage());
        }
    }
}
