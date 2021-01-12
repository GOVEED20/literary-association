package goveed20.LoggingService.controllers;

import goveed20.LoggingService.services.LogService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    public void createLog(@RequestBody LogDTO log) {
        logService.createLog(log);
    }
}
