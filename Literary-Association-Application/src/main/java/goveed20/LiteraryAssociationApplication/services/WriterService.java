package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.enums.MembershipApplicationStatus;
import goveed20.LiteraryAssociationApplication.utils.ReviewResult;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WriterService {
    private static final String writingsFolder = "Literary-Association-Application/src/main/resources/writings/";

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    public void submitWritings(FormSubmissionDTO formSubmissionDTO) {
        if (formSubmissionDTO.getID() == null)
            throw new BusinessProcessException("Missing task id");

        Task task = taskService.createTaskQuery().taskId(formSubmissionDTO.getID()).singleResult();

        if (task == null)
            throw new BusinessProcessException(String.format("Task with id '%s' not found", formSubmissionDTO.getID()));

        try {
            List<String> writingNames = formSubmissionDTO.getFormFields().stream().map(this::parseAndSaveWriting).collect(Collectors.toList());
            runtimeService.setVariable(task.getProcessInstanceId(), "writings", writingNames);

            Map<String, Object> map = UtilService.mapListToDto(formSubmissionDTO.getFormFields());
            formService.submitTaskForm(task.getId(), map);
        } catch (BusinessProcessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessProcessException("Failed to save writings");
        }
    }

    @SuppressWarnings("unchecked")
    public void submitWritingsReview(FormSubmissionDTO formSubmissionDTO) {
        if (formSubmissionDTO.getID() == null)
            throw new BusinessProcessException("Missing task id");

        Task task = taskService.createTaskQuery().taskId(formSubmissionDTO.getID()).singleResult();

        if (task == null)
            throw new BusinessProcessException(String.format("Task with id '%s' not found", formSubmissionDTO.getID()));

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


    @SneakyThrows
    private String parseAndSaveWriting(FormSubmissionFieldDTO formField) {
        byte[] pdfDecoded = Base64.getDecoder().decode(formField.getFieldValue());
        if (pdfDecoded[0] != 0x25 || pdfDecoded[1] != 0x50 || pdfDecoded[2] != 0x44 || pdfDecoded[3] != 0x46) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }

        String writingTitle = String.format("writing-%s", UUID.randomUUID().toString().replace("-", ""));
        File filePaper = new File(String.format("%s%s.pdf", writingsFolder, writingTitle));
        OutputStream os = new FileOutputStream(filePaper);
        os.write(pdfDecoded);
        os.flush();
        os.close();

        return writingTitle;
    }
}
