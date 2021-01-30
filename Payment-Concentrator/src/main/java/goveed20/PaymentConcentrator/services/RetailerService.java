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
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ServiceFieldsCheck;
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
import java.util.UUID;

@Service
public class RetailerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FeignClientBuilder feignClientBuilder;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private EmailService emailService;

    public String registerRetailer(RetailerData retailerData) throws Exception {
        Retailer retailer;
        if ((retailerData.getRetailerName() != null && !retailerData.getRetailerName().equals("")) ||
                (retailerData.getRetailerEmail() != null && !retailerData.getRetailerEmail().equals(""))) {
            retailer = Retailer.builder()
                    .name(retailerData.getRetailerName())
                    .email(retailerData.getRetailerEmail())
                    .build();
        } else {
            throw new BadRequestException("You must provide retailer name and email");
        }

        if (retailerRepository.findByName(retailerData.getRetailerName()).isPresent()) {
            throw new BadRequestException("Given retailer name is already in use");
        }

        if (retailerData.getPaymentServices().size() == 0) {
            throw new BadRequestException("You must select at least one payment service");
        }

        for (PaymentServiceData paymentServiceData : retailerData.getPaymentServices()) {
            Set<PaymentData> paymentDataSet = new HashSet<>();
            PluginRetailerController service = feignClientBuilder.forType(PluginRetailerController.class, paymentServiceData.getServiceName()).build();

            ResponseEntity<ServiceFieldsCheck> validationResponse = service.checkPaymentServiceFields(paymentServiceData.getData());

            if (validationResponse.getBody() != null && validationResponse.getBody().getValidationMessage() == null) {
                for (RegistrationFieldForm paymentField : validationResponse.getBody().getAdditionalFields()) {
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
                throw new BadRequestException(validationResponse.getBody().getValidationMessage());
            }

            RetailerDataForPaymentService retailerDataForPaymentService = RetailerDataForPaymentService.builder()
                    .paymentService(paymentServiceData.getServiceName())
                    .paymentData(paymentDataSet)
                    .retailer(retailer)
                    .build();

            retailer.getRetailerDataForPaymentServices().add(retailerDataForPaymentService);
        }

        String registrationToken = UUID.randomUUID().toString();
        retailer.setRegistrationToken(registrationToken);

        retailerRepository.save(retailer);
        emailService.sendEmail(retailer.getEmail(), "Registration successful",
                String.format("Dear %s, \n You registration on payment concentrator system was successful. \n" +
                        "Your registration token is %s. \n Have a nice day!", retailer.getName(), registrationToken));
        return "Registration was successful";
    }
}
