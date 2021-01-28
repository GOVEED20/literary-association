package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskPreviewDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskType;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskExtensionsService taskExtensionsService;

    @Autowired
    private FormFieldsService formFieldsService;

    public Set<TaskPreviewDTO> getActiveTasksForUser(String username) {
        return taskService
                .createTaskQuery()
                .processVariableValueEquals("user", username)
                .active()
                .list()
                .stream()
                .map(task -> {
                            String bpmnFile = (String) runtimeService.getVariable(task.getProcessInstanceId(), "bpmnFile");

                            return TaskPreviewDTO.builder()
                                    .id(task.getId())
                                    .name(task.getName())
                                    .dueDate((task.getDueDate()))
                                    .blocking(taskExtensionsService.getExtensions(bpmnFile, task.getTaskDefinitionKey())
                                            .containsKey("blocking"))
                                    .build();
                        }
                )
                .collect(Collectors.toSet());
    }

    public TaskDTO getTask(String id) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();

        if (task == null) {
            throw new NotFoundException(String.format("Task with id '%s' not found", id));
        }

        Map<String, String> taskExtensions = taskExtensionsService.getExtensions(
                (String) runtimeService.getVariable(task.getProcessInstanceId(), "bpmnFile"), task.getTaskDefinitionKey());

        TaskDTO dto = new TaskDTO();
        dto.setId(id);
        dto.setName(task.getName());
        dto.setSubmitUrl(taskExtensions.get("submitUrl"));

        if (task.getFormKey() != null) {
            dto.setType(TaskType.FORM);
            dto.setFormFields(getFormFields(task));
        } else {
            dto.setType(TaskType.PAYMENT);
            dto.setTransactionId(null); // supporting only FORM tasks for now
        }

        return dto;
    }

    private List<FormField> getFormFields(Task task) {
        Map<String, String> taskExtensions = taskExtensionsService.getExtensions(
                (String) runtimeService.getVariable(task.getProcessInstanceId(), "bpmnFile"), task.getId());
        if (taskExtensions.containsKey("basic_select")) {
            formFieldsService.setSelectFormFields(task);
        }
        if (taskExtensions.containsKey("genre_select") || taskExtensions.containsKey("beta_reader_select")) {
            formFieldsService.setSerializedFormFields(task);
        }
        if (taskExtensions.containsKey("download_file")) {
            formFieldsService.setDownloadFormField(task);
        }

        return formService.getTaskFormData(task.getId()).getFormFields();
    }

    public void submitForm(FormSubmissionDTO data) {
        Task task = taskService.createTaskQuery().processInstanceId(data.getId()).active().list().get(0);

        if (task == null) {
            task = taskService.createTaskQuery().taskId(data.getId()).singleResult();
        }

        if (task == null) {
            throw new NotFoundException(String.format("Task or process with id '%s' not found", data.getId()));
        }

        Map<String, Object> map = UtilService.mapListToDto(data.getFormFields());
        runtimeService.setVariable(task.getProcessInstanceId(), "data", UtilService.mapListToDto(data.getFormFields()));
        formService.submitTaskForm(task.getId(), map);
    }
}
