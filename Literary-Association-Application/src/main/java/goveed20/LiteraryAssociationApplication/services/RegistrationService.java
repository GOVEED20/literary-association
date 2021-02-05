package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
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
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    public void verify(String disHash, String pID) {
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
                .orElseThrow(() -> new BusinessProcessException("Does not exist"));
        runtimeService.createSignalEvent("Confirmation_link_signal").executionId(exec.getId()).send();
    }
}
