package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.AdditionalContentDTO;
import goveed20.LiteraryAssociationApplication.dtos.ButtonDTO;
import goveed20.LiteraryAssociationApplication.dtos.WorkingPaperDTO;
import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.*;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormFieldsService {

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BetaReaderStatusRepository betaReaderStatusRepository;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    public void setSelectFormFields(Task task) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        Set<String> options;
        String selectName;
        switch (task.getFormKey()) {
            case "accept_reject_working_paper":
                options = new HashSet<>(Arrays.asList("Accept", "Reject"));
                selectName = "accept_working_paper_option";
                break;
            case "plagiarism_form":
                options = new HashSet<>(Arrays.asList("Plagiarism", "Original"));
                selectName = "plagiarism_option";
                break;
            case "accept_reject_full_paper":
                options = new HashSet<>(Arrays.asList("Accept", "Reject"));
                selectName = "accept_full_paper_option";
                break;
            case "include_beta_readers_form":
                options = new HashSet<>(Arrays.asList("Send", "Do not send"));
                selectName = "include_beta_reader_option";
                break;
            case "editor_request_changes_form":
                options = new HashSet<>(Arrays.asList("Request changes", "Everything is fine"));
                selectName = "editor_request_changes_option";
                break;
            case "vote_form":
                options = new HashSet<>(Arrays.asList("Plagiarism", "Original"));
                selectName = "vote_option";
                break;
            default:
                options = new HashSet<>();
                selectName = "";
        }

        UtilService.setOptions(selectName, options, tfd.getFormFields());
    }

    @SuppressWarnings("unchecked")
    public void setSerializedFormFields(Task task) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        tfd.getFormFields().forEach(p -> {
            switch (p.getId()) {
                case "genre":
                    p.getProperties().put("options", UtilService
                            .serializeGenres(new HashSet<>(genreRepository.findAll())));
                    break;
                case "beta_readers":
                    String title = (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper");
                    WorkingPaper workingPaper = workingPaperRepository.findByTitle(title);
                    p.getProperties().put("options", UtilService
                            .serializeBetaReaders(new HashSet<>(betaReaderStatusRepository
                                    .findByBetaGenresContaining(workingPaper.getGenre()))));
                    break;
                case "editors":
                    p.getProperties().put("options", UtilService.serializeEditors(new HashSet<>(
                            baseUserRepository.findAllByRoleEqualsAndUsernameNot(UserRole.EDITOR, task.getAssignee())
                    )));
                    break;
                case "editor_replacement":
                    Map<String, String> chosenEditors = (Map<String, String>) runtimeService
                            .getVariable(task.getProcessInstanceId(), "chosen_editors");
                    List<String> editors = new ArrayList<>(chosenEditors.keySet());
                    editors.add(task.getAssignee());
                    p.getProperties().put("options", UtilService.serializeEditors(new HashSet<>(
                            baseUserRepository.findAllByRoleEqualsAndUsernameNotIn(UserRole.EDITOR, editors)
                    )));
                    break;
                default:
                    return;
            }
        });
    }

    public void setDownloadFormField(Task task, String value) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        String workingPaperTitle = (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper");
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        List<ButtonDTO> buttons = new ArrayList<>();

        if (!value.equals("2")) {
            ButtonDTO button = ButtonDTO.builder()
                    .id("downloadButton")
                    .label("Download paper: " + workingPaperTitle)
                    .title(workingPaperTitle)
                    .downloadURL(baseUrl + "/book/" + workingPaperTitle + "/download")
                    .build();
            buttons.add(button);
        } else {
            String myBook = (String) runtimeService.getVariable(task.getProcessInstanceId(), "my_book");
            String plagiarismBook = (String) runtimeService.getVariable(task.getProcessInstanceId(), "plagiarism_book");

            ButtonDTO button1 = ButtonDTO.builder()
                    .id("downloadButton1")
                    .label("Download paper: " + myBook)
                    .title(myBook)
                    .downloadURL(baseUrl + "/book/" + myBook + "/download")
                    .build();

            ButtonDTO button2 = ButtonDTO.builder()
                    .id("downloadButton2")
                    .label("Download paper: " + plagiarismBook)
                    .title(plagiarismBook)
                    .downloadURL(baseUrl + "/book/" + plagiarismBook + "/download")
                    .build();

            buttons.add(button1);
            buttons.add(button2);
        }

        tfd.getFormFields().forEach(p -> p.getProperties().put("buttons", UtilService.serializeButtons(buttons)));
    }

    public void setAdditionalContent(Task task, String additionalType) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<WorkingPaper> workingPapers = new ArrayList<>();
        Writer writer = null;

        if (task.getFormKey().equals("choose_editor_form") || task.getFormKey().equals("comparison_note_form")
                || task.getFormKey().equals("vote_form")) {
            String myBookTitle = (String) runtimeService.getVariable(task.getProcessInstanceId(), "my_book");
            String plagiarismBookTitle = (String) runtimeService
                    .getVariable(task.getProcessInstanceId(), "plagiarism_book");

            workingPapers.add(bookRepository.findByTitle(myBookTitle).get());
            workingPapers.add(bookRepository.findByTitle(plagiarismBookTitle).get());
        } else {
            String workingPaperTitle = (String) runtimeService
                    .getVariable(task.getProcessInstanceId(), "working_paper");
            workingPapers.add(workingPaperRepository.findByTitle(workingPaperTitle));
            writer = writerRepository
                    .findByUsername((String) runtimeService.getVariable(task.getProcessInstanceId(), "writer")).get();
        }

        List<AdditionalContentDTO> additionalContent = new ArrayList<>();
        switch (additionalType) {
            case "1":
                additionalContent.add(createAdditionalContent(false, workingPapers, writer, task));
                break;
            case "2":
                additionalContent.add(createAdditionalContent(true, workingPapers, writer, task));
                break;
            case "3":
                additionalContent.add(createAdditionalContent(false, workingPapers, writer, task));
                additionalContent.add(createAdditionalContent(true, workingPapers, writer, task));
                break;
        }

        tfd.getFormFields().get(0).getProperties()
                .put("additional", UtilService.serializeAdditionalContent(additionalContent));
    }

    private AdditionalContentDTO createAdditionalContent(boolean isComment, List<WorkingPaper> workingPapersList, Writer writer, Task task) {
        AdditionalContentDTO additionalContentDTO;
        if (!isComment) {
            List<WorkingPaperDTO> workingPapers = workingPapersList.stream().map(wp ->
                    WorkingPaperDTO.builder()
                            .title(wp.getTitle())
                            .synopsis(wp.getSynopsis())
                            .author(writer != null ? writer.getName() + " " + writer.getSurname() : ((Book) wp)
                                    .getWriter().getName() + " " + ((Book) wp).getWriter().getSurname())
                            .genre(wp.getGenre().getGenre().name())
                            .build()).collect(Collectors.toList());


            additionalContentDTO = AdditionalContentDTO.builder()
                    .isComment(false)
                    .content(workingPapers)
                    .build();
        } else {
            List<String> comments = new ArrayList<>();
            switch (task.getFormKey()) {
                case "paper_change_form":
                    commentRepository
                            .findAllByTypeAndApplicationPapersContaining(CommentType.BETA_READER_COMMENT, workingPapersList
                                    .get(0))
                            .forEach(comment -> comments.add(comment.getContent()));
                    break;
                case "correct_mistakes_form":
                    String lectorComment = (String) runtimeService
                            .getVariable(task.getProcessInstanceId(), "lector_comment");
                    comments.add(lectorComment);
                    break;
                case "correct_suggestions_form":
                    String editorComment = (String) runtimeService
                            .getVariable(task.getProcessInstanceId(), "editor_suggestion_comment");
                    comments.add(editorComment);
                    break;
                case "vote_form":
                    commentRepository
                            .findAllByTypeAndApplicationPapersContaining(CommentType.EDITOR_COMMENT, workingPapersList
                                    .get(0))
                            .forEach(comment -> comments.add(comment.getContent()));
                    break;
            }
            additionalContentDTO = AdditionalContentDTO.builder()
                    .isComment(true)
                    .content(comments)
                    .build();
        }

        return additionalContentDTO;
    }
}
