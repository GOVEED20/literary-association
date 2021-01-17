package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.TaskDTO;
import goveed20.LiteraryAssociationApplication.services.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/task")
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;

    @GetMapping("/{username}/active")
    public ResponseEntity<Set<TaskDTO>> getActiveTasksForUser(@PathVariable String username) {
        return new ResponseEntity<>(userTaskService.getActiveTasksForUser(username), HttpStatus.OK);
    }

}
