package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.enums.MembershipApplicationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewResult {
    private String comment;
    private MembershipApplicationStatus status;
}
