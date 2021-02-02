package goveed20.LiteraryAssociationApplication.utils;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewResult implements Serializable {
    private String comment;
    private ReviewStatus status;
}
