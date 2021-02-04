package goveed20.PaymentConcentrator.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetailerData {
    private String retailerName;
    private String retailerEmail;
    private List<PaymentServiceData> paymentServices;
}
