package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.*;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    private static final String tempFolder = "Literary-Association-Application/src/main/resources/temp/";

    public Set<TaskPreviewDTO> getActiveTasksForUser(String username) {
        return taskService
                .createTaskQuery()
                .taskAssignee(username)
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
        Task task = taskService.createTaskQuery().taskId(id).initializeFormKeys().singleResult();

        if (task == null) {
            throw new NotFoundException(String.format("Task with id '%s' not found", id));
        }

        TaskDTO dto = new TaskDTO();
        dto.setId(id);
        dto.setName(task.getName());

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
                (String) runtimeService.getVariable(task.getProcessInstanceId(), "bpmnFile"), task.getTaskDefinitionKey());
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
        Task task;
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(data.getId()).active().list();

        if (taskList.isEmpty()) {
            task = taskService.createTaskQuery().taskId(data.getId()).singleResult();
        } else {
            task = taskList.get(0);
        }

        if (task == null) {
            throw new NotFoundException(String.format("Task or process with id '%s' not found", data.getId()));
        }

        Map<String, Object> map = UtilService.mapListToDto(processLongTextFields(data.getFormFields()));
        runtimeService.setVariable(task.getProcessInstanceId(), "data", map);
        formService.submitTaskForm(task.getId(), map);
    }

    private List<FormSubmissionFieldDTO> processLongTextFields(List<FormSubmissionFieldDTO> originalFields) {
        originalFields.stream().filter(f -> f.getFieldValue().length() >= 4000).forEach(f -> f.setFieldValue(createTempFileForField(f)));
        return originalFields;
    }

    @SneakyThrows
    private String createTempFileForField(FormSubmissionFieldDTO field) {
        String path = String.format("%s%s.txt", tempFolder, field.getFieldId());
        File tempFile = new File(path);
        FileUtils.writeStringToFile(tempFile, field.getFieldValue());
        return path;
    }
}
