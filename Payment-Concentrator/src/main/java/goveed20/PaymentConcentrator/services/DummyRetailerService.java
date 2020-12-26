package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.model.PaymentData;
import goveed20.PaymentConcentrator.model.Retailer;
import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class DummyRetailerService {
    @Autowired
    private RetailerRepository retailerRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void addDummyRetailer() {
        if (retailerRepository.findByName("Laguna").isEmpty()) {
            System.out.println("Adding dummy retailer");

            Retailer retailer = Retailer.builder()
                    .name("Laguna")
                    .build();

            RetailerDataForPaymentService dataForPaypalService = RetailerDataForPaymentService.builder()
                    .paymentService("paypal-service")
                    .retailer(retailer)
                    .build();

            RetailerDataForPaymentService dataForBitcoinService = RetailerDataForPaymentService.builder()
                    .paymentService("bitcoin-service")
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

            dataForPaypalService.getPaymentData().add(payee);
            dataForBitcoinService.getPaymentData().add(coinGateApiKey);

            retailer.getRetailerDataForPaymentServices().add(dataForPaypalService);
            retailer.getRetailerDataForPaymentServices().add(dataForBitcoinService);

            retailerRepository.save(retailer);

            System.out.println("Dummy retailer added");
        }
    }
}
