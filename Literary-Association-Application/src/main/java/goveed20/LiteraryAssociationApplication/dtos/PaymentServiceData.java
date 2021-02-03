package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentServiceData {
    private String serviceName;
    private List<RegistrationFormField> data;
}
