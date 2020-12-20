package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.model.PaymentData;
import goveed20.PaymentConcentrator.model.Retailer;
import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import goveed20.PaymentConcentrator.repositories.PaymentDataRepository;
import goveed20.PaymentConcentrator.repositories.RetailerDataForPaymentServiceRepository;
import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StartUpService {

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerDataForPaymentServiceRepository retailerDataForPaymentServiceRepository;

    @Autowired
    private PaymentDataRepository paymentDataRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createDataOnStartup() {

        PaymentData paymentData = PaymentData.builder()
                .name("coinGateApiKey")
                .value("gJi77wfVqcFGpFx81gjEBUTPd7Ms4u3wH9_j5qen")
                .build();

        if(paymentDataRepository.findById((long) 1).isEmpty())
            paymentDataRepository.save(paymentData);

        Retailer retailer = Retailer.builder()
                .name("Laguna")
                .build();

        Set<PaymentData> paymentDataSet = new HashSet<>();
        paymentDataSet.add(paymentData);

        Set<RetailerDataForPaymentService> retailerData = new HashSet<>();

        RetailerDataForPaymentService bitcoinData = RetailerDataForPaymentService.builder()
                .retailer(retailer)
                .paymentData(paymentDataSet)
                .paymentService("bitcoin-service")
                .build();

        retailerData.add(bitcoinData);
        retailer.setRetailerDataForPaymentServices(retailerData);

        if(retailerRepository.findById((long) 1).isEmpty())
            retailerRepository.save(retailer);

        if(retailerDataForPaymentServiceRepository.findById((long) 1).isEmpty())
            retailerDataForPaymentServiceRepository.save(bitcoinData);



    }

}
