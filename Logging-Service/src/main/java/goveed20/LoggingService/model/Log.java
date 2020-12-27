package goveed20.LoggingService.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String methodName;

    @Column(nullable = false)
    private String logLevel;

    @Column(nullable = false)
    private String message;
}
