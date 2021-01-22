package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPreviewDTO {
    private String id;
    private String name;
    private Date dueDate;
    // will be used later on private Boolean blocking;
}
