package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.dtos.PaymentServiceData;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginRetailerController;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationFieldForm;
import goveed20.PaymentConcentrator.dtos.RetailerData;
import goveed20.PaymentConcentrator.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.model.PaymentData;
import goveed20.PaymentConcentrator.model.Retailer;
import goveed20.PaymentConcentrator.model.RetailerDataForPaymentService;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PluginController;
import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RetailerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FeignClientBuilder feignClientBuilder;

    @Autowired
    private RetailerRepository retailerRepository;

    public String registerRetailer(RetailerData retailerData) {
        Retailer retailer;
        if (retailerData.getRetailerName() != null && !retailerData.getRetailerName().equals("")) {
            retailer = Retailer.builder()
                    .name(retailerData.getRetailerName())
                    .build();
        } else {
            throw new BadRequestException("You must provide retailer name");
        }

        if (retailerData.getPaymentServices().size() == 0) {
            throw new BadRequestException("You must select at least one payment service");
        }

        for (PaymentServiceData paymentServiceData : retailerData.getPaymentServices()) {
            Set<PaymentData> paymentDataSet = new HashSet<>();
            List<RegistrationFieldForm> list = paymentServiceData.getData();
            String serviceName = paymentServiceData.getServiceName();
            PluginRetailerController service = feignClientBuilder.forType(PluginRetailerController.class, paymentServiceData.getServiceName()).build();

            ResponseEntity<String> validationResponse = service.checkPaymentServiceFields(paymentServiceData.getData());
            if (validationResponse.getStatusCode().equals(HttpStatus.OK) && validationResponse.getBody().equals("ok")) {
                for (RegistrationFieldForm paymentField : paymentServiceData.getData()) {
                    PaymentData paymentData = new PaymentData();
                    if (paymentField.getEncrypted()) {
                        paymentData.setValue(passwordEncoder.encode(paymentField.getValue()));
                    } else {
                        paymentData.setValue(paymentField.getValue());
                    }
                    paymentData.setName(paymentField.getName());
                    paymentDataSet.add(paymentData);

                }
            } else {
                throw new BadRequestException(validationResponse.getBody());
            }

            RetailerDataForPaymentService retailerDataForPaymentService = RetailerDataForPaymentService.builder()
                    .paymentService(paymentServiceData.getServiceName())
                    .paymentData(paymentDataSet)
                    .retailer(retailer)
                    .build();

            retailer.getRetailerDataForPaymentServices().add(retailerDataForPaymentService);
        }

//        retailerData.getPaymentServices().forEach(paymentServiceData -> {
//            Set<PaymentData> paymentDataSet = new HashSet<>();
//            paymentServiceData.getData().forEach(paymentField -> {
//                PaymentData paymentData = new PaymentData();
//                String validationMsg = validateField(paymentField.getName(), paymentField.getType(), paymentField.getValue());
//                if (validationMsg.equals("ok")) {
//                    if (paymentField.getEncrypted()) {
//                        paymentData.setValue(passwordEncoder.encode(paymentField.getValue()));
//                    } else {
//                        paymentData.setValue(paymentField.getValue());
//                    }
//                    paymentData.setName(paymentField.getName());
//                    paymentDataSet.add(paymentData);
//                } else {
//                    throw new BadRequestException(validationMsg);
//                }
//            });
//
//            RetailerDataForPaymentService retailerDataForPaymentService = RetailerDataForPaymentService.builder()
//                    .paymentService(paymentServiceData.getServiceName())
//                    .paymentData(paymentDataSet)
//                    .retailer(retailer)
//                    .build();
//
//            retailer.getRetailerDataForPaymentServices().add(retailerDataForPaymentService);
//
//        });

        retailerRepository.save(retailer);

        return "Registration was successful";
    }
}
