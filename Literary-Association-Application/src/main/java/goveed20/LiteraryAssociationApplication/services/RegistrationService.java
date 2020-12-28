package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.model.VerificationToken;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private GenreRepository genreRepository;

    public FormFieldsDTO getFormFields(String processID) {
        Task task = taskService.createTaskQuery().processInstanceId(processID).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        properties.forEach(p -> {
            if (p.getId().equals("genres") || p.getId().equals("beta_genres")) {
                p.getProperties().put("options", UtilService
                        .serializeGenres(new HashSet<>(genreRepository.findAll())));
            }
        });


        return new FormFieldsDTO(processID, task.getId(), properties);
    }

    public void register(FormSubmissionDTO regData) {
        HashMap<String, Object> map = UtilService.mapListToDto(regData.getFormFields());
        if (readerRepository.findByUsername(String.valueOf(map.get("username"))) != null) {
            throw new BpmnError("User with given username already exists");
        }
        if (readerRepository.findByEmail(String.valueOf(map.get("email"))) != null) {
            throw new BpmnError("User with given email address already exists");
        }

        Task task = taskService.createTaskQuery().processInstanceId(regData.getProcessID()).active().list().get(0);
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", regData.getFormFields());
        formService.submitTaskForm(task.getId(), map); // complete input registration data task
    }

    public void verify(String disHash, String pID) throws Exception {
        VerificationToken vt = verificationTokenRepository.findByDisposableHash(disHash);
        if (vt == null) {
            throw new EntityNotFoundException("The link is invalid or broken!");
        }

        Reader reader = readerRepository.findByEmail(vt.getUser().getEmail());
        if (reader != null) {
            reader.setVerified(true);
            readerRepository.save(reader);
        } else {
            Writer w = writerRepository.findByEmail(vt.getUser().getEmail());
            w.setVerified(true);
            writerRepository.save(w);
        }

        Execution exec = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("Confirmation_link_signal")
                .list().stream().filter(e -> e.getProcessInstanceId().equals(pID)).findFirst()
                .orElseThrow(() -> new Exception("Does not exist"));
        runtimeService.createSignalEvent("Confirmation_link_signal").executionId(exec.getId()).send();
    }
}
