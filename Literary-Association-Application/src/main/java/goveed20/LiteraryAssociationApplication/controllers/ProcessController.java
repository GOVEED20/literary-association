package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping("/start")
    public ResponseEntity<String> startProcess(@RequestBody String processName) {
        return new ResponseEntity<>(processService.startProcess(processName), HttpStatus.OK);
    }
}
