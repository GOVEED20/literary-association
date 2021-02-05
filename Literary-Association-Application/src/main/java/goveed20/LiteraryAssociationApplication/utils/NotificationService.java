package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.dtos.NotificationDTO;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    public BpmnError sendErrorNotification(String error) {
        template.convertAndSend("/notification", NotificationDTO.builder()
                .notification(error)
                .notificationType("error")
                .build());
        return new BpmnError(error);
    }

    public void sendSuccessNotification(String success) {
        template.convertAndSend("/notification", NotificationDTO.builder()
                .notification(success)
                .notificationType("success")
                .build());
    }
}
