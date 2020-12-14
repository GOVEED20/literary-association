package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FormSubmissionDTO implements Serializable {
    private List<FormSubmissionFieldDTO> formFields;
    private String taskID;
}
