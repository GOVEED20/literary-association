package goveed20.LoggingService.services;

import goveed20.LoggingService.model.Log;
import goveed20.LoggingService.repositories.LogRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void createLog(LogDTO logDTO) {
        Log log = Log.builder()
                .date(logDTO.getDate())
                .serviceName(logDTO.getServiceName())
                .className(logDTO.getClassName())
                .methodName(logDTO.getMethodName())
                .logLevel(logDTO.getLogLevel())
                .message(logDTO.getMessage())
                .build();
        logRepository.save(log);
    }

}
