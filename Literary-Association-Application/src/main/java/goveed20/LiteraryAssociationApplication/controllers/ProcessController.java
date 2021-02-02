package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/process")
@CrossOrigin
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping("/start")
    @PreAuthorize("!(hasAuthority('READER') or hasAuthority('WRITER') or hasAuthority('LECTOR') or hasAuthority('BOARD_MEMBER') or hasAuthority('EDITOR'))")
    public ResponseEntity<String> startProcess(@RequestBody String processName) {
        return new ResponseEntity<>(processService.startProcess(processName), HttpStatus.OK);
    }
}
