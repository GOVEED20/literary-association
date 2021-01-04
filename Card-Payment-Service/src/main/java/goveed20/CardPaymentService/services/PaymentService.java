package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.exceptions.BadRequestException;
import goveed20.CardPaymentService.exceptions.InvalidBankClientDataException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.model.Transaction;
import goveed20.CardPaymentService.repositories.BankRepository;
import goveed20.CardPaymentService.repositories.ClientRepository;
import goveed20.CardPaymentService.repositories.TransactionRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PaymentConcentratorFeignClient;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankService bankService;

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    public String initializePayment(InitializationPaymentPayload payload) {

        if (payload.getPaymentFields() == null || !payload.getPaymentFields().containsKey("MERCHANT_ID")
                || !payload.getPaymentFields().containsKey("MERCHANT_PASSWORD")
                || !payload.getPaymentFields().containsKey("ACQUIRER_BANK")
                || !payload.getPaymentFields().containsKey("MERCHANT_ORDER_ID")
                || !payload.getPaymentFields().containsKey("MERCHANT_TIMESTAMP")) {
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

    public void completePayment(Long transactionID, Map<String, String[]> paramMap) {
        String[] pan = paramMap.getOrDefault("pan", null);
        String[] bankName = paramMap.getOrDefault("bankName", null);
        String[] securityCode = paramMap.getOrDefault("secCode", null);
        String[] cardholder = paramMap.getOrDefault("cardholder", null);
        String[] expiryDate = paramMap.getOrDefault("expiryDate", null);

        if (pan == null || bankName == null || securityCode == null || cardholder == null || expiryDate == null) {
            throw new BadRequestException("Missing parameters");
        } else if (!pan[0].matches("([0-9]{4}-){3}[0-9]{4}")) {
            throw new BadRequestException("Invalid PAN format");
        } else if (!expiryDate[0].matches("((0[1-9])|(1[0-2]))/[0-9]{4}")) {
            throw new BadRequestException("Invalid expiry date format");
        }

        Transaction transaction = transactionRepository.findByTransactionID(transactionID.toString())
                .orElseThrow(() -> new BadRequestException("Transaction does not exist"));
        Bank bank = bankRepository.findByName(bankName[0])
                .orElseThrow(() -> new BadRequestException("Bank does not exist"));

        // check bank
        String bankIdentifier = pan[0].substring(0, 4) + pan[0].substring(5, 7);

        if (!bank.getBankIdentifier().equals(bankIdentifier)) {
            // call PCC
        } else {
            Client customer = null;
            Double amount = transaction.getAmount();
            try {
                customer = bankService.checkClientsAccount(bank, pan[0], securityCode[0], cardholder[0], expiryDate[0],
                        amount);
            } catch (InvalidBankClientDataException e) {
                Map<String, String> paymentData = new HashMap<>();
                paymentData.put("MERCHANT_ORDER_ID", transaction.getMerchantOrderID());
                paymentData.put("PAYMENT_ID", transaction.getPaymentID());
                sendTransactionResponse(transactionID, TransactionStatus.FAILED, paymentData);
            }

            customer.setBalance(customer.getBalance() - amount);
            Client merchant = bank.getClients().stream().filter(c -> c.getOnlinePaymentData().getMerchantID()
                    .equals(transaction.getMerchantID())).findFirst().get();
            merchant.setBalance(merchant.getBalance() + amount);

            String acquirerOrderID = String.valueOf(new Date());
            String acquirerTimestamp = UUID.randomUUID().toString();

            Map<String, String> paymentData = new HashMap<>();
            paymentData.put("MERCHANT_ORDER_ID", transaction.getMerchantOrderID());
            paymentData.put("ACQUIRER_ORDER_ID", acquirerOrderID);
            paymentData.put("ACQUIRER_TIMESTAMP", acquirerTimestamp);
            paymentData.put("PAYMENT_ID", transaction.getPaymentID());

            transaction.setAcquirerOrderID(acquirerOrderID);
            transaction.setAcquirerTimestamp(acquirerTimestamp);

            clientRepository.save(customer);
            clientRepository.save(merchant);
            transactionRepository.save(transaction);

            sendTransactionResponse(transactionID, TransactionStatus.SUCCESS, paymentData);
        }
    }

    @Async
    @SneakyThrows
    public void sendTransactionResponse(Long transactionId, TransactionStatus status, Map<String, String> paymentData) {
        paymentConcentratorFeignClient.sendTransactionResponse(
                ResponsePayload.builder()
                        .transactionID(transactionId)
                        .transactionStatus(status)
                        .paymentData(paymentData)
                        .build()
        );
    }
}
