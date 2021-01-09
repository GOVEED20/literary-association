package goveed20.BitcoinPaymentService.services;

import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetailerService {

    public String checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        for (RegistrationFieldForm registrationFieldForm : payload) {
            if (registrationFieldForm.getName().equals("coinGateApiKey")) {
                if (registrationFieldForm.getValue() == null || registrationFieldForm.getValue().equals("")) {
                    throw new BadRequestException("CoinGate API Key must be provided");
                }
            }
        }

        return "ok";
    }
}
