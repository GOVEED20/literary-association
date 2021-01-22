package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.TaskDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskPreviewDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskType;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    // add support for blocking tasks later
    public Set<TaskPreviewDTO> getActiveTasksForUser(String username) {
        return taskService
                .createTaskQuery()
                .processVariableValueEquals("user", username)
                .active()
                .list()
                .stream()
                .map(task -> TaskPreviewDTO.builder()
                        .id(task.getId())
                        .name(task.getName())
                        .dueDate((task.getDueDate())
                        ).build()
                )
                .collect(Collectors.toSet());
    }

    public TaskDTO getTask(String id) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();

        if (task == null) {
            throw new NotFoundException(String.format("Task with id '%s' not found", id));
        }

        TaskDTO dto = new TaskDTO();
        dto.setId(id);

        if (task.getFormKey() != null) {
            dto.setType(TaskType.FORM);
            dto.setFormFields(formService.getTaskFormData(id).getFormFields());
        } else {
            dto.setType(TaskType.PAYMENT);
            dto.setTransactionId(null); // supporting only FORM tasks for now
        }

        return dto;
    }
}
