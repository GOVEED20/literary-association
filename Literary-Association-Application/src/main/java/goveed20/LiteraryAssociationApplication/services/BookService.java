package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.apache.commons.io.FilenameUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class BookService {

    private static String booksFolder = "Literary-Association-Application/src/main/resources/workingPapers/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    public String submitWorkingPaperTemplate(FormSubmissionDTO paper) {
        Map<String, Object> map = UtilService.mapListToDto(paper.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(paper.getProcessID()).active().list().get(0);

        runtimeService.setVariable(paper.getProcessID(), "working_paper", map.get("title"));
        formService.submitTaskForm(task.getId(), map);

        WorkingPaper workingPaper = WorkingPaper.workingPaperBuilder().title((String) map.get("title"))
                .synopsis((String) map.get("synopsis")).genre(Genre.builder()
                        .genre(GenreEnum.valueOf((String) map.get("genre"))).build()).build();
        workingPaperRepository.save(workingPaper);

        return "Working paper successfully submitted";
    }

    public String acceptOrRejectWorkingPaper(FormSubmissionDTO options) {
        Map<String, Object> map = UtilService.mapListToDto(options.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(options.getProcessID()).active().list().get(0);

        boolean accepted = map.get("accept_option").equals("Accept");
        runtimeService.setVariable(options.getProcessID(), "accepted", accepted);
        formService.submitTaskForm(task.getId(), map);

        return "Working paper successfully " + (accepted ? "accepted" : "rejected");
    }

    public String inputRejectionComment(FormSubmissionDTO comment) {
        Map<String, Object> map = UtilService.mapListToDto(comment.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(comment.getProcessID()).active().list().get(0);

        runtimeService.setVariable(comment.getProcessID(), "rejectionComment", map.get("rejection_comment"));
        formService.submitTaskForm(task.getId(), map);

        return "Rejection comment successfully sent";
    }

    public String submitFullWorkingPaper(String processID, MultipartFile file) throws IOException {
        Task task = taskService.createTaskQuery().processInstanceId(processID).active().list().get(0);

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null || !extension.equals("pdf")) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }

        String workingPaperTitle = (String) runtimeService.getVariable(processID, "working_paper");
        WorkingPaper paper = workingPaperRepository.findByTitle(workingPaperTitle);
        File filePaper = new File(booksFolder + workingPaperTitle + ".pdf");
        try (OutputStream os = new FileOutputStream(filePaper)) {
            os.write(file.getBytes());
        }
        paper.setFile(filePaper.getPath());

        Map<String, Object> map = new HashMap<>();
        map.put("full_paper", file.getName());
        formService.submitTaskForm(task.getId(), map);
        workingPaperRepository.save(paper);

        return "Full working paper successfully submitted";
    }
}
