package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.repositories.BankRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RetailerService {

    @Autowired
    private BankRepository bankRepository;

    public ServiceFieldsCheck checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        ServiceFieldsCheck serviceFieldsCheck = new ServiceFieldsCheck();
        List<RegistrationFieldForm> checkedFields = new ArrayList<>();
        serviceFieldsCheck.setAdditionalFields(checkedFields);
        RegistrationFieldForm panField;

        panField = payload.stream().filter(f -> f.getName().equals("PAN")).findFirst()
                .orElseThrow(() -> new BadRequestException("PAN not provided"));
        String pan = panField.getValue();
        if (!pan.matches("([0-9]{4}-){3}[0-9]{4}")) {
            throw new BadRequestException("Invalid PAN format");
        }

        String bankIdentifier = pan.substring(0, 4) + pan.substring(5, 7);
        Bank bank = bankRepository.findByBankIdentifier(bankIdentifier)
                .orElseThrow(() -> new BadRequestException("Customer's bank does not exist"));
        if (bank.getClients().stream().noneMatch(f -> pan.equals(f.getPAN()))) {
            throw new BadRequestException("Customer with given PAN does not exist");
        }

        serviceFieldsCheck.getAdditionalFields().add(panField);
        serviceFieldsCheck.getAdditionalFields()
                .add(RegistrationFieldForm.builder().name("MERCHANT_BANK").value(bank.getName())
                        .encrypted(false).build());
        serviceFieldsCheck.getAdditionalFields()
                .add(RegistrationFieldForm.builder().name("MERCHANT_ID").value(UUID.randomUUID().toString())
                        .encrypted(false).build());
        serviceFieldsCheck.getAdditionalFields()
                .add(RegistrationFieldForm.builder().name("MERCHANT_PASSWORD").value(UUID.randomUUID().toString())
                        .encrypted(true).build());

        serviceFieldsCheck.setValidationMessage("ok");
        return serviceFieldsCheck;
    }
}
