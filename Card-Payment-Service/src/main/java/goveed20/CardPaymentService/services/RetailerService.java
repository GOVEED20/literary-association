package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.exceptions.InvalidBankClientDataException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.model.OnlinePaymentData;
import goveed20.CardPaymentService.repositories.BankRepository;
import goveed20.CardPaymentService.repositories.ClientRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RetailerService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ServiceFieldsCheck checkPaymentServiceFields(List<RegistrationFieldForm> payload) {
        ServiceFieldsCheck serviceFieldsCheck = new ServiceFieldsCheck();
        serviceFieldsCheck.setAdditionalFields(new ArrayList<>());
        String validationMessage = null;

        try {
            RegistrationFieldForm panField = payload.stream().filter(f -> f.getName().equals("PAN")).findFirst()
                    .orElseThrow(() -> new BadRequestException("PAN not provided"));
            String pan = panField.getValue();

            if (!pan.matches("([0-9]{4}-){3}[0-9]{4}")) {
                throw new BadRequestException("Invalid PAN format");
            }

            String bankIdentifier = pan.substring(0, 4) + pan.substring(5, 7);
            Bank bank = bankRepository.findByBankIdentifier(bankIdentifier)
                    .orElseThrow(() -> new BadRequestException("Client's bank does not exist"));
            Client client = bank.getClients().stream().filter(c -> encoder.matches(pan, c.getPAN())).findFirst()
                    .orElseThrow(() -> new BadRequestException("Client with given PAN does not exist"));

            String merchantID = UUID.randomUUID().toString();
            String merchantPassword = encoder.encode(UUID.randomUUID().toString());

            serviceFieldsCheck.getAdditionalFields().add(panField);
            serviceFieldsCheck.getAdditionalFields()
                    .add(RegistrationFieldForm.builder().name("MERCHANT_BANK").value(bank.getName())
                            .encrypted(false).build());
            serviceFieldsCheck.getAdditionalFields()
                    .add(RegistrationFieldForm.builder().name("MERCHANT_ID").value(merchantID)
                            .encrypted(false).build());
            serviceFieldsCheck.getAdditionalFields()
                    .add(RegistrationFieldForm.builder().name("MERCHANT_PASSWORD").value(merchantPassword)
                            .encrypted(false).build());

            OnlinePaymentData oyp = OnlinePaymentData.builder().merchantID(merchantID)
                    .merchantPassword(merchantPassword).build();
            client.setOnlinePaymentData(oyp);

            clientRepository.save(client);
        } catch (Exception e) {
            validationMessage = e.getMessage();
        }

        serviceFieldsCheck.setValidationMessage(validationMessage);
        return serviceFieldsCheck;
    }
}
