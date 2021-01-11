package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Card;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.model.OnlinePaymentData;
import goveed20.CardPaymentService.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StartupService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private PasswordEncoder encoder;

    @EventListener(ApplicationReadyEvent.class)
    public void addEntities() {
        if (bankRepository.findByName("UniCredit").isEmpty()) {
            Bank unicredit = Bank.builder().name("UniCredit").bankIdentifier("123456")
                    .clients(new ArrayList<>()).build();
            Bank komercijalna = Bank.builder().name("Komercijalna").bankIdentifier("987654")
                    .clients(new ArrayList<>()).build();

            // merchant
            Client merchant = Client.builder().balance(100.00).PAN(encoder
                    .encode("1234-5611-1111-1111")).build();
            unicredit.getClients().add(merchant);

            // customer unicredit
            Card card1 = Card.builder().securityCode(encoder.encode("123")).cardHolderName("Satoshi")
                    .expiryDate("07/2024").build();
            Client customerUnicredit = Client.builder().balance(50.00).card(card1)
                    .PAN(encoder.encode("1234-5611-2222-3333")).build();
            unicredit.getClients().add(customerUnicredit);

            // customer komercijalna
            Card card2 = Card.builder().securityCode(encoder.encode("456")).cardHolderName("Vitalik")
                    .expiryDate("06/2023").build();
            Client customerKomercijalna = Client.builder().balance(50.00).card(card2)
                    .PAN(encoder.encode("9876-5411-2222-3333")).build();
            komercijalna.getClients().add(customerKomercijalna);

            bankRepository.save(unicredit);
            bankRepository.save(komercijalna);
        }
    }
}
