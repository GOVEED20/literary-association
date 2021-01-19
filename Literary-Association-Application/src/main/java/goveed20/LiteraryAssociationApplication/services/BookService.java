package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionDTO;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    public String submitWorkingPaperTemplate(FormSubmissionDTO paper) {
        Map<String, Object> map = UtilService.mapListToDto(paper.getFormFields());
        Task task = taskService.createTaskQuery().processInstanceId(paper.getProcessID()).active().list().get(0);

        runtimeService.setVariable(paper.getProcessID(), "working_paper", map.get("title"));
        formService.submitTaskForm(task.getId(), map); // complete input registration data task

        return "Working paper successfully submitted";
    }
}
