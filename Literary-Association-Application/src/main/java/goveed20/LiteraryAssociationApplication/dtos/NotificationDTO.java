package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String notification;
    private String notificationType;
}
