package goveed20.BitcoinPaymentService.services;

import goveed20.BitcoinPaymentService.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetailerService {

    public ServiceFieldsCheck checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        ServiceFieldsCheck serviceFieldsCheck = new ServiceFieldsCheck();
        List<RegistrationFieldForm> checkedFields = new ArrayList<>();
        serviceFieldsCheck.setAdditionalFields(checkedFields);
        for (RegistrationFieldForm registrationFieldForm : payload) {
            if (registrationFieldForm.getName().equals("coinGateApiKey")) {
                if (registrationFieldForm.getValue() == null || registrationFieldForm.getValue().equals("")) {
                    throw new BadRequestException("CoinGate API Key must be provided");
                }
            }
            serviceFieldsCheck.getAdditionalFields().add(registrationFieldForm);
        }

        serviceFieldsCheck.setValidationMessage("ok");
        return serviceFieldsCheck;
    }
}
