package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.dtos.TaskPreviewDTO;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.services.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;

    @GetMapping("/{username}/active")
    public ResponseEntity<Set<TaskPreviewDTO>> getActiveTasksForUser(@PathVariable String username) {
        return new ResponseEntity<>(userTaskService.getActiveTasksForUser(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(userTaskService.getTask(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> submitForm(@RequestBody FormSubmissionDTO data) {
        try {
            userTaskService.submitForm(data);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
