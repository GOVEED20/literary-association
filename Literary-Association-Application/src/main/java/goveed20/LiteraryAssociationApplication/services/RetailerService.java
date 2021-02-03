package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.RetailerData;
import goveed20.LiteraryAssociationApplication.exceptions.BadRequestException;
import goveed20.LiteraryAssociationApplication.model.Retailer;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.utils.PaymentUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetailerService {

    @Autowired
    private PaymentUtilsService paymentUtilsService;

    @Autowired
    private RetailerRepository retailerRepository;

    public Object getAvailableServices() {
        return paymentUtilsService.getAvailableServices().getBody();
    }

    public Object getServiceRegistrationFields(String serviceName) {
        return paymentUtilsService.getServiceRegistrationFields(serviceName).getBody();
    }

    public String registerRetailer(RetailerData retailerData) {
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

        if (retailerRepository.findByEmail(retailerData.getRetailerEmail()).isPresent()) {
            throw new BadRequestException("Given retailer email is already in use");
        }

        if (retailerData.getPaymentServices().size() == 0) {
            throw new BadRequestException("You must select at least one payment service");
        }

        try {
            paymentUtilsService.sendRegisterRetailerRequest(retailerData);
        } catch (Exception e) {
            String msg = e.getMessage();
            throw new BadRequestException(msg.substring(msg.indexOf("[") + 1, msg.lastIndexOf("]")));
        }

        retailerRepository.save(retailer);

        return "Retailer registered successfully";
    }
}
