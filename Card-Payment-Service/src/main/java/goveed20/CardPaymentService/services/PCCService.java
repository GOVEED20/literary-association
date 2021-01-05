package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.dtos.CustomersBankResponseDTO;
import goveed20.CardPaymentService.exceptions.InvalidBankClientDataException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Transaction;
import goveed20.CardPaymentService.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PCCService {

    @Autowired
    private BankService bankService;

    @Autowired
    private BankRepository bankRepository;

    CustomersBankResponseDTO callPCC(String pan, String securityCode, String cardholder, String expiryDate,
                                     String acquirerOrderID, String acquirerTimestamp, Transaction transaction) {

        String bankIdentifier = pan.substring(0, 4) + pan.substring(5, 7);

        Bank bank = bankRepository.findByBankIdentifier(bankIdentifier).orElseThrow(() ->
                new InvalidBankClientDataException("Customer's bank does not exist"));

        return bankService.completePaymentInCustomersBank(transaction, bank, pan, securityCode,
                cardholder, expiryDate, acquirerOrderID, acquirerTimestamp);
    }
}
