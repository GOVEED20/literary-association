package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.dtos.CardholderDataDTO;
import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.repositories.BankRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private BankRepository bankRepository;

    public String initializePayment(InitializationPaymentPayload payload) {

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("MERCHANT_ID")
                || !payload.getPaymentFields().containsKey("MERCHANT_PASSWORD")
                || !payload.getPaymentFields().containsKey("ACQUIRER_BANK")) {
            throw new BadRequestException("Missing merchant data");
        }

        Bank bank = bankRepository.findByName(payload.getPaymentFields().get("ACQUIRER_BANK")).orElseThrow(()
                -> new BadRequestException("Merchant's bank does not exist"));

        String merchantID = payload.getPaymentFields().get("MERCHANT_ID");
        String merchantPassword = payload.getPaymentFields().get("MERCHANT_PASSWORD");

        Client client = bank.getClients().stream().filter(c -> c.getOnlinePaymentData() != null
                && c.getOnlinePaymentData().getMerchantID().equals(merchantID)).findFirst().orElseThrow(()
                -> new BadRequestException("Merchant does not exist"));

        if (!client.getOnlinePaymentData().getMerchantPassword().equals(merchantPassword)) {
            throw new BadRequestException("Merchant's password is incorrect");
        }

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String paymentID = UUID.randomUUID().toString();

        return baseUrl + "/bank-page/" + bank.getName() + "/" + paymentID + "/" + payload.getTransactionId();
    }

    public void completePayment(Long transactionId, Map<String, String[]> paramMap) {
        System.out.println(transactionId);
    }
}
