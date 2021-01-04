package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.exceptions.InvalidBankClientDataException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BankService {

    @Autowired
    private ClientRepository clientRepository;

    Client checkClientsAccount(Bank bank, String pan, String securityCode, String cardholder, String expiryDate,
                             Double amount){
        Client client = bank.getClients().stream().filter(c -> c.getPAN().equals(pan)).findFirst()
                .orElseThrow(() -> new InvalidBankClientDataException("Client with given PAN does not exist"));

        if (!client.getCard().getSecurityCode().equals(securityCode)) {
            throw new InvalidBankClientDataException("Invalid security code");
        }
        else if (!client.getCard().getCardHolderName().equals(cardholder)) {
            throw new InvalidBankClientDataException("Invalid cardholder");
        }
        else if (!client.getCard().getExpiryDate().equals(expiryDate)) {
            throw new InvalidBankClientDataException("Invalid card expiry date");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        Date expiry;
        try {
            expiry = sdf.parse(expiryDate);
        } catch (ParseException e) {
            throw new InvalidBankClientDataException("Invalid card expiry date");
        }

        if (expiry.compareTo(new Date()) < 0) {
            throw new InvalidBankClientDataException("Card has expired");
        }

        if (client.getBalance() < amount) {
            throw new InvalidBankClientDataException("Insufficient funds in the account");
        }

        return client;
    }
}
