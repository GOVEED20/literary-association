package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.CommentRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class BookService {

    private static final String booksFolder = "Literary-Association-Application/src/main/resources/workingPapers/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    public String submitWorkingPaperTemplate(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();
        String title = (String) map.get("title");
        if (workingPaperRepository.findByTitle(title) != null) {
            throw new BpmnError("Book with given title already exists");
        }

        runtimeService.setVariable(task.getProcessInstanceId(), "working_paper", title);
        formService.submitTaskForm(task.getId(), map);

        WorkingPaper workingPaper = WorkingPaper.workingPaperBuilder().title(title)
                .synopsis((String) map.get("synopsis")).genre(Genre.builder()
                        .genre(GenreEnum.valueOf((String) map.get("genre"))).build()).build();
        workingPaperRepository.save(workingPaper);

        return "Working paper successfully submitted";
    }

    public String chooseBetaReaders(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();
        Set<String> betaReaders = UtilService.parseBetaReaders((String) map.get("beta_readers"));
        if (betaReaders.size() == 0) {
            return "There are no selected beta readers";
        }

        runtimeService.setVariable(task.getProcessInstanceId(), "beta_readers", new ArrayList<>(betaReaders));
        formService.submitTaskForm(task.getId(), map);
        return "Beta readers chosen successfully";
    }

    public String publishBook(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();
        WorkingPaper workingPaper = workingPaperRepository.findByTitle(
                (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper"));
        if (workingPaper == null) {
            throw new BpmnError("Working paper with given title does not exist");
        }

        formService.submitTaskForm(task.getId(), map);

        Book book = Book.bookBuilder()
                .title(workingPaper.getTitle())
                .synopsis(workingPaper.getSynopsis())
                .genre(workingPaper.getGenre())
                .file(workingPaper.getFile())
                .ISBN((String) map.get("isbn"))
                .keywords((String) map.get("keywords"))
                .publisher((String) map.get("publisher"))
                .publicationYear((Integer.parseInt((String) map.get("publication_year"))))
                .publicationPlace((String) map.get("publication_place"))
                .pages((Integer.parseInt((String) map.get("pages"))))
                .price(Double.parseDouble((String) map.get("price")))
                .status(WorkingPaperStatus.APPROVED)
                .build();
        bookRepository.save(book);

        return "Book successfully published";
    }

    public String submitBetaReaderComment(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();

        BaseUser writer = (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (writer == null) {
            return "There is no logged-in user";
        }

        String workingPaperTitle = (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper");
        WorkingPaper workingPaper = workingPaperRepository.findByTitle(workingPaperTitle);
        if (workingPaper == null) {
            return "Working paper that was commented is not found";
        }
        Set<ApplicationPaper> paperSet = new HashSet<>();
        paperSet.add(workingPaper);
        Comment comment = Comment.builder()
                .applicationPapers(paperSet)
                .content((String) map.get("beta_reader_comment"))
                .type(CommentType.BETA_READER_COMMENT)
                .user(writer)
                .build();
        commentRepository.save(comment);
        formService.submitTaskForm(task.getId(), map);

        return "Comment successfully sent";
    }

    public String submitSelectFormFields(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();
        String retVal;
        String processVariable;
        boolean processVariableValue;

        switch (task.getFormKey()) {
            case "accept_reject_working_paper":
                processVariable = "working_paper_accepted";
                processVariableValue = map.get("accept_option").equals("Accept");
                retVal = "Working paper successfully " + (processVariableValue ? "accepted" : "rejected");
                break;
            case "plagiarism_form":
                processVariable = "is_plagiarism";
                processVariableValue = map.get("plagiarism_option").equals("Plagiarism");
                retVal = "Working paper successfully marked as " + (processVariableValue ? "plagiarism" : "original");
                break;
            case "accept_reject_full_paper":
                processVariable = "full_paper_accepted ";
                processVariableValue = map.get("accept_paper_option").equals("Accept");
                retVal = "Full working paper successfully " + (processVariableValue ? "accepted" : "rejected");
                break;
            case "include_beta_readers_form":
                processVariable = "include_beta_readers  ";
                processVariableValue = map.get("include_beta_reader_option").equals("Send");
                retVal = "";
                break;
            case "editor_request_changes_form":
                processVariable = "editor_requested_changes ";
                processVariableValue = map.get("editor_request_changes").equals("Request changes");
                retVal = "Changes " + (processVariableValue ? "requested" : "not requested" + " for given paper");
                break;
            default:
                retVal = "";
                processVariable = "";
                processVariableValue = false;
        }

        runtimeService.setVariable(task.getProcessInstanceId(), processVariable, processVariableValue);
        formService.submitTaskForm(task.getId(), map);

        return retVal;
    }

    public String submitCommentForm(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();
        String retVal;
        String processVariableCommentName;
        String processVariableCommentValue;

        switch (task.getFormKey()) {
            case "working_paper_reject_form":
                processVariableCommentName = "working_paper_rejection_comment";
                processVariableCommentValue = (String) map.get("rejection_comment");
                retVal = "Rejection comment successfully sent";
                break;
            case "plagiarism_reject_form":
                processVariableCommentName = "paper_plagiarism_reject_comment";
                processVariableCommentValue = (String) map.get("plagiarism_reject_comment");
                retVal = "Plagiarism rejection comment successfully sent";
                break;
            case "full_paper_reject_form":
                processVariableCommentName = "paper_rejection_comment";
                processVariableCommentValue = (String) map.get("full_paper_rejection_comment");
                retVal = "Rejection comment successfully sent";
                break;
            case "mistakes_form":
                processVariableCommentName = "lector_comment";
                processVariableCommentValue = (String) map.get("mistake_comment");
                retVal = "Comment for lexicographic mistakes successfully sent";
                runtimeService.setVariable(formSubmission.getID(), "correct_mistakes",
                        !map.get("mistake_comment").equals(""));
                break;
            case "editor_suggestions_form":
                processVariableCommentName = "editor_suggestion_comment";
                processVariableCommentValue = (String) map.get("editor_suggestions");
                retVal = "Suggestions on working paper successfully delivered";
                runtimeService.setVariable(formSubmission.getID(), "editor_suggested",
                        !map.get("editor_suggestions").equals(""));
                break;
            default:
                retVal = "";
                processVariableCommentName = "";
                processVariableCommentValue = "";
        }

        runtimeService
                .setVariable(task.getProcessInstanceId(), processVariableCommentName, processVariableCommentValue);
        formService.submitTaskForm(task.getId(), map);

        return retVal;
    }

    public String submitFile(FormSubmissionDTO formSubmission) {
        Map<String, Object> map = UtilService.mapListToDto(formSubmission.getFormFields());
        Task task = taskService.createTaskQuery().taskId(formSubmission.getID()).singleResult();

        String retVal;
        String formFieldName;

        switch (task.getFormKey()) {
            case "full_paper_form":
                formFieldName = "full_paper";
                retVal = "Full working paper successfully submitted";
                break;
            case "paper_change_form":
                formFieldName = "changed_paper";
                retVal = "Full working paper successfully changed";
                break;
            case "correct_mistakes_form":
                formFieldName = "corrected_file";
                retVal = "Full working paper successfully corrected";
                break;
            case "correct_suggestions_form":
                formFieldName = "correct_suggestions";
                retVal = "Full working paper successfully corrected according to suggestions";
                break;
            default:
                formFieldName = "";
                retVal = "";
        }

        WorkingPaper paper;
        try {
            paper = submitPaper(task.getProcessInstanceId(), (String) map.get(formFieldName));
        } catch (BusinessProcessException | EntityNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return "Working paper not found";
        }

        formService.submitTaskForm(task.getId(), map);
        workingPaperRepository.save(paper);

        return retVal;
    }

    private WorkingPaper submitPaper(String processID, String base64File) throws IOException {
        byte[] pdfDecoded = Base64.getDecoder().decode(base64File);
        if (pdfDecoded[0] != 0x25 || pdfDecoded[1] != 0x50 || pdfDecoded[2] != 0x44 || pdfDecoded[3] != 0x46) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }

        String workingPaperTitle = (String) runtimeService.getVariable(processID, "working_paper");
        WorkingPaper paper = workingPaperRepository.findByTitle(workingPaperTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Working paper is not found");
        }

        File filePaper = new File(booksFolder + workingPaperTitle + ".pdf");
        OutputStream os = new FileOutputStream(filePaper);
        os.write(pdfDecoded);
        os.flush();
        os.close();

        paper.setFile(filePaper.getPath());
        return paper;
    }

    public String downloadBook(String bookTitle) throws Exception {
        WorkingPaper paper = workingPaperRepository.findByTitle(bookTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Book with given title does not exist");
        }

        File file = new File(paper.getFile());
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(paper.getTitle() + ".pdf");
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.close();
        } catch (IOException e) {
            throw new Exception("Book file does not exist");
        }

        return "Successful download";
    }
}
