package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormFieldsDTO;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    public FormFieldsDTO getFormFields() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Reader_registration");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(pi.getId(), task.getId(), properties);
    }

    public void register(FormSubmissionDTO regData) {
        HashMap<String, Object> map = UtilService.mapListToDto(regData.getFormFields());
        Task task = taskService.createTaskQuery().taskId(regData.getTaskID()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", regData.getFormFields());
        formService.submitTaskForm(regData.getTaskID(), map); // complete input registration data task

        if (Boolean.parseBoolean(map.get("beta_reader").toString())) {
            task = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().list().get(0);
            String beta_genres = map.get("beta_genres").toString();
            map.clear();
            map.put("beta_genres", beta_genres);
            formService.submitTaskForm(task.getId(), map); // complete select beta genres task
        }
    }

    public void verify() {
        runtimeService.createSignalEvent("Confirmation link signal").send();
        assert(taskService.createTaskQuery().active().list().size() == 0);
    }
}
