package goveed20.CardPaymentService.services;

import goveed20.CardPaymentService.dtos.CustomersBankResponseDTO;
import goveed20.CardPaymentService.exceptions.InvalidBankClientDataException;
import goveed20.CardPaymentService.model.Bank;
import goveed20.CardPaymentService.model.Client;
import goveed20.CardPaymentService.model.Transaction;
import goveed20.CardPaymentService.repositories.ClientRepository;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.PaymentConcentratorFeignClient;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.ResponsePayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class BankService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentConcentratorFeignClient paymentConcentratorFeignClient;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm");

    public Client checkClientsAccount(Bank bank, String pan, String securityCode, String cardholder, String expiryDate,
                               Double amount) {
        Client client = bank.getClients().stream().filter(c -> c.getPAN().equals(pan)).findFirst()
                .orElseThrow(() -> new InvalidBankClientDataException("Client with given PAN does not exist"));

        if (!client.getCard().getSecurityCode().equals(securityCode)) {
            throw new InvalidBankClientDataException("Invalid security code");
        } else if (!client.getCard().getCardHolderName().equals(cardholder)) {
            throw new InvalidBankClientDataException("Invalid cardholder");
        } else if (!client.getCard().getExpiryDate().equals(expiryDate)) {
            throw new InvalidBankClientDataException("Invalid card expiry date");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        Date expiry;
        try {
            expiry = sdf.parse(expiryDate);
        } catch (ParseException e) {
            throw new InvalidBankClientDataException("Invalid card expiry date");
        }

        if (expiry.compareTo(new Date()) < 0) {
            throw new InvalidBankClientDataException("Card has expired");
        }

        if (client.getBalance() < amount) {
            throw new InvalidBankClientDataException("Insufficient funds in the account");
        }

        return client;
    }

    public CustomersBankResponseDTO completePaymentInCustomersBank(Transaction transaction, Bank bank,
                                                            String pan, String securityCode, String cardholder,
                                                            String expiryDate, String acquirerOrderID,
                                                            String acquirerTimestamp) {
        Client client;
        String errorMessage = null;
        Double amount = transaction.getAmount();
        try {
            client = checkClientsAccount(bank, pan, securityCode, cardholder, expiryDate,
                    amount);
            client.setBalance(client.getBalance() - amount);
            clientRepository.save(client);
        } catch (InvalidBankClientDataException e) {
            errorMessage = e.getMessage();
        }

        return CustomersBankResponseDTO.builder().acquirerOrderID(acquirerOrderID)
                .acquirerTimestamp(acquirerTimestamp).issuerOrderID(UUID.randomUUID().toString())
                .issuerTimestamp(formatter.format(new Date())).errorMessage(errorMessage).build();
    }

    public Transaction completePaymentInMerchantsBank(Long transactionID, Transaction transaction, Bank bank, String pan,
                                                      String securityCode, String cardholder, String expiryDate,
                                                      CustomersBankResponseDTO customersBankResponse) {

        Client customer;
        Double amount = transaction.getAmount();

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("MERCHANT_ORDER_ID", transaction.getMerchantOrderID());
        paymentData.put("PAYMENT_ID", transaction.getPaymentID());

        String acquirerOrderID;
        String acquirerTimestamp;
        String issuerOrderID = null;
        String issuerTimestamp = null;

        if (customersBankResponse == null) {
            acquirerOrderID = UUID.randomUUID().toString();
            acquirerTimestamp = formatter.format(new Date());
        } else {
            acquirerOrderID = customersBankResponse.getAcquirerOrderID();
            acquirerTimestamp = customersBankResponse.getAcquirerTimestamp();
            issuerOrderID = customersBankResponse.getIssuerOrderID();
            issuerTimestamp = customersBankResponse.getIssuerTimestamp();
        }
        paymentData.put("ACQUIRER_ORDER_ID", acquirerOrderID);
        paymentData.put("ACQUIRER_TIMESTAMP", acquirerTimestamp);
        transaction.setAcquirerOrderID(acquirerOrderID);
        transaction.setAcquirerTimestamp(acquirerTimestamp);
        transaction.setIssuerOrderID(issuerOrderID);
        transaction.setIssuerTimestamp(issuerTimestamp);


        if (customersBankResponse == null) {
            try {
                customer = checkClientsAccount(bank, pan, securityCode, cardholder, expiryDate,
                        amount);
                customer.setBalance(customer.getBalance() - amount);
                Client merchant = bank.getClients().stream().filter(c -> c.getOnlinePaymentData().getMerchantID()
                        .equals(transaction.getMerchantID())).findFirst().get();
                merchant.setBalance(merchant.getBalance() + amount);

                clientRepository.save(customer);
                clientRepository.save(merchant);

                sendTransactionResponse(transactionID, TransactionStatus.SUCCESS, paymentData);
            } catch (InvalidBankClientDataException e) {
                sendTransactionResponse(transactionID, TransactionStatus.FAILED, paymentData);
            }

            return transaction;
        } else if (customersBankResponse.getErrorMessage() != null) {
            sendTransactionResponse(transactionID, TransactionStatus.FAILED, paymentData);
            return transaction;
        }

        Client merchant = bank.getClients().stream().filter(c -> c.getOnlinePaymentData().getMerchantID()
                .equals(transaction.getMerchantID())).findFirst().get();
        merchant.setBalance(merchant.getBalance() + amount);

        clientRepository.save(merchant);
        sendTransactionResponse(transactionID, TransactionStatus.SUCCESS, paymentData);


        return transaction;
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
