package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.TaskDTO;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTaskService {

    @Autowired
    private TaskService taskService;

    public Set<TaskDTO> getActiveTasksForUser(String username) {
        return taskService
                .createTaskQuery()
                .processVariableValueEquals("user", username)
                .active()
                .list()
                .stream()
                .map(task -> TaskDTO.builder().id(task.getId()).name(task.getName()).dueDate(task.getDueDate()).build())
                .collect(Collectors.toSet());
    }
}
