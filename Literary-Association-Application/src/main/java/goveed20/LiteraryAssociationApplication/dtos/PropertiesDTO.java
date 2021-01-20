package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PropertiesDTO {
    private List<FormField> properties;
    private String taskID;
}
