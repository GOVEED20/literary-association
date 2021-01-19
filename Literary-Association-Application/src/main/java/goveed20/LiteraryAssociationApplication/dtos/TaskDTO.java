package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private String id;
    private String name;
    private Date dueDate;
}
