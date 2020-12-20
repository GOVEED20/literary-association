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
        System.out.println("Adding dummy retailer");

        Retailer retailer = Retailer.builder()
                .name("dummy")
                .build();

        RetailerDataForPaymentService data = RetailerDataForPaymentService.builder()
                .paymentService("paypal-service")
                .retailer(retailer)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .name("payee")
                .value("sb-rqo034159139@business.example.com")
                .build();

        data.getPaymentData().add(paymentData);
        retailer.getRetailerDataForPaymentServices().add(data);

        retailerRepository.save(retailer);

        System.out.println("Dummy retailer added");
    }
}
