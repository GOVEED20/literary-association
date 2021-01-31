package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.model.*;
import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import goveed20.PaymentConcentrator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DummyRetailerService {
    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void addDummyRetailer() {
        if (retailerRepository.findByName("Laguna").isEmpty()) {

            Retailer retailer = Retailer.builder()
                    .name("Laguna")
                    .email("laguna@maildrop.cc")
                    .registrationToken("2565a484-e734-4477-bbb0-18e0a0d1afbe")
                    .build();

            RetailerDataForPaymentService dataForPaypalService = RetailerDataForPaymentService.builder()
                    .paymentService("paypal-service")
                    .retailer(retailer)
                    .build();

            RetailerDataForPaymentService dataForBitcoinService = RetailerDataForPaymentService.builder()
                    .paymentService("bitcoin-service")
                    .retailer(retailer)
                    .build();

            RetailerDataForPaymentService dataForCardPaymentService = RetailerDataForPaymentService.builder()
                    .paymentService("bank-service")
                    .retailer(retailer)
                    .build();

            PaymentData payee = PaymentData.builder()
                    .name("payee")
                    .value("sb-rqo034159139@business.example.com")
                    .build();

            PaymentData coinGateApiKey = PaymentData.builder()
                    .name("coinGateApiKey")
                    .value("gJi77wfVqcFGpFx81gjEBUTPd7Ms4u3wH9_j5qen")
                    .build();

            PaymentData merchantID = PaymentData.builder()
                    .name("MERCHANT_ID")
                    .value("123456789")
                    .build();

            PaymentData merchantPassword = PaymentData.builder()
                    .name("MERCHANT_PASSWORD")
                    .value("$2a$10$NqK3gW9/ZYzWvQID0GEbzuOcyglZg3jZtju.StvmetohqpMj9o09O")
                    .build();

            PaymentData bank = PaymentData.builder()
                    .name("MERCHANT_BANK")
                    .value("UniCredit")
                    .build();

            dataForPaypalService.getPaymentData().add(payee);
            dataForBitcoinService.getPaymentData().add(coinGateApiKey);
            dataForCardPaymentService.getPaymentData().add(merchantID);
            dataForCardPaymentService.getPaymentData().add(merchantPassword);
            dataForCardPaymentService.getPaymentData().add(bank);

            retailer.getRetailerDataForPaymentServices().add(dataForPaypalService);
            retailer.getRetailerDataForPaymentServices().add(dataForBitcoinService);
            retailer.getRetailerDataForPaymentServices().add(dataForCardPaymentService);

            User admin = User.builder()
                    .name("Admin")
                    .surname("Admirovic")
                    .username("admin")
                    .password(passwordEncoder.encode("Admin123!"))
                    .role(UserRole.ADMIN)
                    .build();

            retailerRepository.save(retailer);
            userRepository.save(admin);

        }
    }
}
