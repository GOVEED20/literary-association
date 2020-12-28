package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FormSubmissionFieldDTO implements Serializable {
    private String fieldId;
    private String fieldValue;
}
