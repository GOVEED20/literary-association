package goveed20.LiteraryAssociationApplication.services;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    public String startProcess(String processName) {
        return runtimeService.startProcessInstanceByKey(processName).getId();
    }
}
