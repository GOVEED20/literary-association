package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LogDTO {
    private Date date;
    private String serviceName;
    private String className;
    private String methodName;
    private String logLevel;
    private String message;

}
