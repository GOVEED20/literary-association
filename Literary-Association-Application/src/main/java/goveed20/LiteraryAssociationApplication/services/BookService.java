package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.dtos.PropertiesDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.apache.commons.io.FilenameUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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

    @Autowired
    private GenreRepository genreRepository;

    private PropertiesDTO getProperties(String processID) {
        Task task = taskService.createTaskQuery().processInstanceId(processID).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());

        return PropertiesDTO.builder().properties(tfd.getFormFields()).taskID(task.getId()).build();
    }

    public FormFieldsDTO getFormFieldsForSubmittingWorkingPaperTemplate(String processID) {
        Task task = taskService.createTaskQuery().processInstanceId(processID).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        properties.forEach(p -> {
            if (p.getId().equals("genres")) {
                p.getProperties().put("options", UtilService
                        .serializeGenres(new HashSet<>(genreRepository.findAll())));
            }
        });

        return new FormFieldsDTO(processID, task.getId(), properties);
    }

    public String submitWorkingPaperTemplate(FormSubmissionDTO paper) {
        Map<String, Object> map = UtilService.mapListToDto(paper.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(paper.getProcessID()).active().list().get(0);

        String title = (String) map.get("title");
        if (workingPaperRepository.findByTitle(title) != null) {
            throw new BpmnError("Book with given title already exists");
        }

        runtimeService.setVariable(paper.getProcessID(), "working_paper", map.get("title"));
        formService.submitTaskForm(task.getId(), map);

        WorkingPaper workingPaper = WorkingPaper.workingPaperBuilder().title((String) map.get("title"))
                .synopsis((String) map.get("synopsis")).genre(Genre.builder()
                        .genre(GenreEnum.valueOf((String) map.get("genre"))).build()).build();
        workingPaperRepository.save(workingPaper);

        return "Working paper successfully submitted";
    }

    public FormFieldsDTO getFormFieldsForAcceptingOrRejectingWorkingPaperTemplate(String processID) {
        PropertiesDTO properties = getProperties(processID);
        Set<String> options = new HashSet<>();
        options.add("Accept");
        options.add("Reject");

        UtilService.setOptions("accept_option", options, properties.getProperties());

        return new FormFieldsDTO(processID, properties.getTaskID(), properties.getProperties());
    }

    public String acceptOrRejectWorkingPaper(FormSubmissionDTO options) {
        Map<String, Object> map = UtilService.mapListToDto(options.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(options.getProcessID()).active().list().get(0);

        boolean accepted = map.get("accept_option").equals("Accept");
        runtimeService.setVariable(options.getProcessID(), "accepted", accepted);
        formService.submitTaskForm(task.getId(), map);

        return "Working paper successfully " + (accepted ? "accepted" : "rejected");
    }

    /* For rejection comment, plagiarism comment, full paper rejection comment */
    public FormFieldsDTO getFormFieldsForComments(String processID) {
        Task task = taskService.createTaskQuery().processInstanceId(processID).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());

        return new FormFieldsDTO(processID, task.getId(), tfd.getFormFields());
    }

    public String inputRejectionComment(FormSubmissionDTO comment) {
        Map<String, Object> map = UtilService.mapListToDto(comment.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(comment.getProcessID()).active().list().get(0);

        runtimeService.setVariable(comment.getProcessID(), "rejectionComment", map.get("rejection_comment"));
        formService.submitTaskForm(task.getId(), map);

        return "Rejection comment successfully sent";
    }

    public FormFieldsDTO getFormFieldsForSubmittingFullWorkingPaper(String processID) {
        Task task = taskService.createTaskQuery().processInstanceId(processID).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());

        return new FormFieldsDTO(processID, task.getId(), tfd.getFormFields());
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

    public FormFieldsDTO getFormFieldsForDecidingIfPaperIsPlagiarism(String processID) {
        PropertiesDTO properties = getProperties(processID);
        Set<String> options = new HashSet<>();
        options.add("Plagiarism");
        options.add("Original");

        UtilService.setOptions("plagiarism_option", options, properties.getProperties());

        return new FormFieldsDTO(processID, properties.getTaskID(), properties.getProperties());
    }

    public String decideIfPaperIsPlagiarism(FormSubmissionDTO options) {
        Map<String, Object> map = UtilService.mapListToDto(options.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(options.getProcessID()).active().list().get(0);

        boolean isPlagiarism = map.get("plagiarism_option").equals("Plagiarism");
        runtimeService.setVariable(options.getProcessID(), "is_plagiarism", isPlagiarism);
        formService.submitTaskForm(task.getId(), map);

        return "Working paper successfully marked as " + (isPlagiarism ? "plagiarism" : "original");
    }

    public String plagiarismCommentInput(FormSubmissionDTO comment) {
        Map<String, Object> map = UtilService.mapListToDto(comment.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(comment.getProcessID()).active().list().get(0);

        runtimeService.setVariable(comment.getProcessID(), "plagiarism_reject_comment",
                map.get("plagiarism_reject_comment"));
        formService.submitTaskForm(task.getId(), map);

        return "Plagiarism rejection comment successfully sent";
    }

    public FormFieldsDTO getFormFieldsForAcceptingOrRejectingFullWorkingPaper(String processID) {
        PropertiesDTO properties = getProperties(processID);
        Set<String> options = new HashSet<>();
        options.add("Accept");
        options.add("Reject");

        UtilService.setOptions("accept_paper_option", options, properties.getProperties());

        return new FormFieldsDTO(processID, properties.getTaskID(), properties.getProperties());
    }

    public String acceptOrRejectFullWorkingPaper(FormSubmissionDTO options) {
        Map<String, Object> map = UtilService.mapListToDto(options.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(options.getProcessID()).active().list().get(0);

        boolean accepted = map.get("accept_paper_option").equals("Accept");
        runtimeService.setVariable(options.getProcessID(), "accepted_full_paper", accepted);
        formService.submitTaskForm(task.getId(), map);

        return "Working paper successfully " + (accepted ? "accepted" : "rejected");
    }

    public String rejectionOfFullPaperCommentInput(FormSubmissionDTO comment) {
        Map<String, Object> map = UtilService.mapListToDto(comment.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(comment.getProcessID()).active().list().get(0);

        runtimeService.setVariable(comment.getProcessID(), "full_paper_rejection_comment",
                map.get("full_paper_rejection_comment"));
        formService.submitTaskForm(task.getId(), map);

        return "Rejection comment successfully sent";
    }

    public FormFieldsDTO getFormFieldsForIncludingBetaReaders(String processID) {
        PropertiesDTO properties = getProperties(processID);
        Set<String> options = new HashSet<>();
        options.add("Send");
        options.add("Do not send");

        UtilService.setOptions("include_beta_reader_option", options, properties.getProperties());

        return new FormFieldsDTO(processID, properties.getTaskID(), properties.getProperties());
    }

    public void includeBetaReaders(FormSubmissionDTO options) {
        Map<String, Object> map = UtilService.mapListToDto(options.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(options.getProcessID()).active().list().get(0);

        boolean sendToBetaReaders = map.get("include_beta_reader_option").equals("Send");
        runtimeService.setVariable(options.getProcessID(), "include_beta_readers", sendToBetaReaders);
        formService.submitTaskForm(task.getId(), map);
    }
}
