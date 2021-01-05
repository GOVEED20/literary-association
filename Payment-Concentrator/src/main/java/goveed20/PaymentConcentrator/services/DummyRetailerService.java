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

            RetailerDataForPaymentService dataForCardPaymentService = RetailerDataForPaymentService.builder()
                    .paymentService("card-payment-service")
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

            PaymentData merchantID = PaymentData.builder().name("MERCHANT_ID").value("retailerID").build();
            PaymentData merchantPassword = PaymentData.builder().name("MERCHANT_PASSWORD").value("satipo").build();
            PaymentData bank = PaymentData.builder().name("MERCHANT_BANK").value("UniCredit").build();

            dataForPaypalService.getPaymentData().add(payee);
            dataForBitcoinService.getPaymentData().add(coinGateApiKey);
            dataForCardPaymentService.getPaymentData().add(merchantID);
            dataForCardPaymentService.getPaymentData().add(merchantPassword);
            dataForCardPaymentService.getPaymentData().add(bank);

            retailer.getRetailerDataForPaymentServices().add(dataForPaypalService);
            retailer.getRetailerDataForPaymentServices().add(dataForBitcoinService);
            retailer.getRetailerDataForPaymentServices().add(dataForCardPaymentService);

            retailerRepository.save(retailer);

        }
    }
}
