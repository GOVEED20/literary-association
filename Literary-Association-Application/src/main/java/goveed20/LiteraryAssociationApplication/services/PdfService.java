package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.enums.MembershipApplicationStatus;
import goveed20.LiteraryAssociationApplication.utils.ReviewResult;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PdfService {
    private static final String writingsFolder = "Literary-Association-Application/src/main/resources/writings/";

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @SuppressWarnings("unchecked")
    public void submitWritingsReview(FormSubmissionDTO formSubmissionDTO) {
        if (formSubmissionDTO.getId() == null)
            throw new BusinessProcessException("Missing task id");

        Task task = taskService.createTaskQuery().taskId(formSubmissionDTO.getId()).singleResult();

        if (task == null)
            throw new BusinessProcessException(String.format("Task with id '%s' not found", formSubmissionDTO.getId()));

        Map<String, Object> map = UtilService.mapListToDto(formSubmissionDTO.getFormFields());
        Map<String, ReviewResult> reviewResults = (Map<String, ReviewResult>) runtimeService.getVariable(task.getProcessInstanceId(), "review_results");
        reviewResults.replace(
                task.getAssignee(),
                ReviewResult.builder()
                        .comment((String) map.get("comment"))
                        .status(MembershipApplicationStatus.valueOf((String) map.get("status")))
                        .build()
        );

        runtimeService.setVariable(task.getProcessInstanceId(), "review_results", reviewResults);
        formService.submitTaskForm(task.getId(), map);
    }

    public List<String> saveBase64ToPdf(List<String> base64Strings) {
        return saveBase64ToPdf((String[]) base64Strings.toArray());
    }

    public List<String> saveBase64ToPdf(String[] base64Strings) {
        return Arrays.stream(base64Strings).sequential().map(s -> {
            if (!s.contains("data:application/pdf;base64,")) {
                throw new BusinessProcessException("Invalid file type. It should be a PDF file");
            }
            s = s.replace("data:application/pdf;base64,", "");
            byte[] decoded = Base64.getMimeDecoder().decode(s.getBytes(StandardCharsets.UTF_8));
            String title = String.format("pdf-%s", UUID.randomUUID().toString().replace("-", ""));
            String path = String.format("%s%s.pdf", writingsFolder, title);
            try {
                FileUtils.writeByteArrayToFile(new File(path), decoded);
            } catch (IOException e) {
                throw new BusinessProcessException("Failed to save pdf");
            }
            return title;
        }).collect(Collectors.toList());
    }
}
