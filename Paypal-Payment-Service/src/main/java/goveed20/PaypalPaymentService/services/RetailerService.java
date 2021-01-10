package goveed20.PaypalPaymentService.services;

import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
import goveed20.PaypalPaymentService.exceptions.BadRequestException;
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
            if (registrationFieldForm.getName().equals("payee")) {
                if (registrationFieldForm.getValue() == null || registrationFieldForm.getValue().equals("")) {
                    throw new BadRequestException("Payee email must be provided");
                } else if (!isValidEmailAddress(registrationFieldForm.getValue())) {
                    throw new BadRequestException("Bad payee email format");
                }
            }
            serviceFieldsCheck.getAdditionalFields().add(registrationFieldForm);
        }

        serviceFieldsCheck.setValidationMessage("ok");
        return serviceFieldsCheck;
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
