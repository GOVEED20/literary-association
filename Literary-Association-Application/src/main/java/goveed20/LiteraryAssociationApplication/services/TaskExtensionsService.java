package goveed20.LiteraryAssociationApplication.services;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelException;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskExtensionsService {

    public Map<String, String> getExtensions(String bpmnFile, String taskId) {
        String bpmnFilePath = String.format("Literary-Association-Application/src/main/resources/%s.bpmn", bpmnFile);
        File file = new File(bpmnFilePath);
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

        UserTask task = modelInstance.getModelElementById(taskId);
        CamundaProperties properties;
        try {
            properties = task.getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class)
                    .singleResult();
        } catch (BpmnModelException e) {
            return new HashMap<>();
        }

        return properties.getCamundaProperties().stream()
                .collect(
                        Collectors.toMap(CamundaProperty::getCamundaName, CamundaProperty::getCamundaValue)
                );
    }
}
