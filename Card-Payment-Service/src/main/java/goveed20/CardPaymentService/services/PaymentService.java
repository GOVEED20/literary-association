package goveed20.CardPaymentService.services;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public String initializePayment(InitializationPaymentPayload payload) {
        return "aaa";
    }

}
