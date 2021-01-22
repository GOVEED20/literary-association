package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private TaskType type;
    private String id;
    private List<FormField> formFields;
    private Long transactionId;
    private String submitUrl;
}
