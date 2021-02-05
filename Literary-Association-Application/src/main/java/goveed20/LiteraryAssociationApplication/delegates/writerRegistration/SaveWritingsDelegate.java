package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.services.PdfService;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class SaveWritingsDelegate implements JavaDelegate {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        String writingsPath = String.valueOf(data.get("writings"));
        File writingsFile = new File(writingsPath);
        String writingsString = FileUtils.readFileToString(writingsFile);
        FileUtils.forceDelete(writingsFile);

        String[] writings = writingsString.split(" ");

        if (writings.length < 2) {
            throw notificationService.sendErrorNotification("At least 2 writings should be submitted");
        }

        try {
            List<String> writingsPaths = pdfService.saveBase64ToPdf(writings);
            delegateExecution.setVariable("documents", writingsPaths);
        } catch (BusinessProcessException e) {
            throw notificationService.sendErrorNotification(e.getMessage());
        }

        notificationService.sendSuccessNotification("Writings successfully uploaded");
    }
}
