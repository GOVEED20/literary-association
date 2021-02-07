package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.dtos.CustomersBankResponseDTO;
import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.model.Transaction;
import goveed20.CardPaymentService.repositories.BankRepository;
import goveed20.CardPaymentService.repositories.TransactionRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.RegistrationField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankService bankService;

    @Autowired
    private PCCService pccService;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");

    public String initializePayment(InitializationPaymentPayload payload) {

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("MERCHANT_ID")
                || !payload.getPaymentFields().containsKey("MERCHANT_PASSWORD")
                || !payload.getPaymentFields().containsKey("MERCHANT_BANK")
                || !payload.getPaymentFields().containsKey("MERCHANT_ORDER_ID")
                || !payload.getPaymentFields().containsKey("MERCHANT_TIMESTAMP")) {
            throw new BadRequestException("Missing merchant data");
        }

        Bank bank = bankRepository.findByName(payload.getPaymentFields().get("MERCHANT_BANK")).orElseThrow(()
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

        Transaction transaction = Transaction.builder()
                .transactionID(payload.getTransactionId().toString())
                .paymentID(paymentID).merchantID(merchantID)
                .merchantOrderID(payload.getPaymentFields().get("MERCHANT_ORDER_ID"))
                .merchantTimestamp(payload.getPaymentFields().get("MERCHANT_TIMESTAMP"))
                .amount(payload.getAmount())
                .build();

        transactionRepository.save(transaction);

        return baseUrl + "/bank-page/" + bank.getName() + "/" + payload.getTransactionId();
    }

    public String completePayment(Long transactionID, HttpServletRequest request) {
        if (request.getHeaderNames() == null) {
            throw new BadRequestException("Missing parameters");
        }
        String pan = request.getHeader("pan");
        String securityCode = request.getHeader("secCode");
        String cardholder = request.getHeader("cardholder");
        String expiryDate = request.getHeader("expiryDate");
        String[] bankName = request.getParameterMap().getOrDefault("bankName", null);

        if (pan == null || bankName == null || securityCode == null || cardholder == null || expiryDate == null) {
            throw new BadRequestException("Missing parameters");
        } else if (!pan.matches("([0-9]{4}-){3}[0-9]{4}")) {
            throw new BadRequestException("Invalid PAN format");
        } else if (!expiryDate.matches("((0[1-9])|(1[0-2]))/[0-9]{4}")) {
            throw new BadRequestException("Invalid expiry date format");
        }

        Transaction transaction = transactionRepository.findByTransactionID(transactionID.toString())
                .orElseThrow(() -> new BadRequestException("Transaction does not exist"));

        if (transaction.isCompleted()) {
            throw new BadRequestException("Pending transaction with given ID does not exist");
        }

        Bank bank = bankRepository.findByName(bankName[0])
                .orElseThrow(() -> new BadRequestException("Bank does not exist"));

        // check bank
        String bankIdentifier = pan.substring(0, 4) + pan.substring(5, 7);

        CustomersBankResponseDTO customersBankResponse = null;
        if (!bank.getBankIdentifier().equals(bankIdentifier)) {
            customersBankResponse = pccService.callPCC(pan, securityCode, cardholder, expiryDate,
                    UUID.randomUUID().toString(), sdf.format(new Date()), transaction);
        }

        transaction = bankService
                .completePaymentInMerchantsBank(transactionID, transaction, bank, pan, securityCode,
                        cardholder, expiryDate, customersBankResponse);

        transaction.setCompleted(true);
        transactionRepository.save(transaction);

        return "Transaction is completed successfully";
    }

    public Set<RegistrationField> getPaymentServiceRegistrationFields() {
        Map<String, String> validationConstraints = new HashMap<>();
        validationConstraints.put("type", "text");
        validationConstraints.put("required", "required");
        validationConstraints.put("pattern", "([0-9]{4}-){3}[0-9]{4}");
        validationConstraints.put("minLength", "19");
        validationConstraints.put("maxLength", "19");

        Set<RegistrationField> regFields = new HashSet<>();
        regFields.add(RegistrationField.builder().name("PAN")
                .validationConstraints(validationConstraints).encrypted(true).build());

        return regFields;
    }
}
