package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.services.WriterService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/writer")
public class WriterController {
    @Autowired
    private WriterService writerService;

    @PostMapping("/writings")
    public ResponseEntity<?> submitWritings(@RequestBody FormSubmissionDTO formSubmissionDTO) {
        try {
            writerService.submitWritings(formSubmissionDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessProcessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/writings/review")
    public ResponseEntity<?> submitWritingsReview(@RequestBody FormSubmissionDTO formSubmissionDTO) {
        try {
            writerService.submitWritingsReview(formSubmissionDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessProcessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
