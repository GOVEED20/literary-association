package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.ButtonDTO;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.BetaReaderStatusRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.utils.CustomFormField;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

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
            default:
                options = new HashSet<>();
                selectName = "";
        }

        UtilService.setOptions(selectName, options, tfd.getFormFields());
    }

    public void setSerializedFormFields(Task task) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        tfd.getFormFields().forEach(p -> {
            if (p.getId().equals("genre")) {
                p.getProperties().put("options", UtilService
                        .serializeGenres(new HashSet<>(genreRepository.findAll())));
            } else if (p.getId().equals("beta_readers")) {
                String title = (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper");
                WorkingPaper workingPaper = workingPaperRepository.findByTitle(title);
                p.getProperties().put("options", UtilService
                        .serializeBetaReaders(new HashSet<>(betaReaderStatusRepository
                                .findByGenre(workingPaper.getGenre()))));
            }
        });
    }

    public void setDownloadFormField(Task task) {
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        String workingPaperTitle = (String) runtimeService.getVariable(task.getProcessInstanceId(), "working_paper");
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        ButtonDTO button = ButtonDTO.builder()
                .id("downloadButton")
                .label("Download paper: " + workingPaperTitle)
                .title(workingPaperTitle)
                .downloadURL(baseUrl + "/book/" + workingPaperTitle + "/download")
                .build();
        List<ButtonDTO> buttons = new ArrayList<>();
        buttons.add(button);

        tfd.getFormFields().forEach(p -> p.getProperties().put("buttons", UtilService.serializeButtons(buttons)));
    }
}
