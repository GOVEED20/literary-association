package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.InvoiceDTO;
import goveed20.LiteraryAssociationApplication.dtos.InvoiceItemDTO;
import goveed20.LiteraryAssociationApplication.dtos.OrderDTO;
import goveed20.LiteraryAssociationApplication.dtos.PaymentFieldsDTO;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.exceptions.PaymentException;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.repositories.TransactionRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String CALLBACK_URL = "/transaction/%d/%s";

    public String initializeTransaction(InvoiceDTO invoiceDTO) {
        Invoice invoice = Invoice.builder()
                .retailer(getRetailer(invoiceDTO.getRetailer()))
                .invoiceItems(invoiceDTO.getInvoiceItems().stream().map(this::createInvoiceItem).collect(Collectors.toSet()))
                .build();

        Transaction transaction = Transaction.builder()
                .initializedOn(new Date())
                .invoice(invoice)
                .done(false)
                .paidWith(invoiceDTO.getPaymentMethod())
                .status(TransactionStatus.INITIALIZED)
                .total(getTotal(invoice))
                .build();

        invoice.setTransaction(transaction);

        transaction = transactionRepository.save(transaction);

        OrderDTO orderDTO = OrderDTO.builder()
                .amount(transaction.getTotal())
                .errorURL(buildCallbackUrl(transaction.getId(), "error"))
                .failedURL(buildCallbackUrl(transaction.getId(), "failed"))
                .successURL(buildCallbackUrl(transaction.getId(), "success"))
                .retailer(invoice.getRetailer().getName())
                .transactionId(transaction.getId())
                .paymentFields(PaymentFieldsDTO.builder().subscription(invoiceDTO.getSubscription()).name(invoiceDTO.getSubscription() ? "LA Membership Subscription" : null).build())
                .build();

        String url = String.format("http://localhost:8080/api/payment-services/%s/initialize-payment", invoiceDTO.getPaymentMethod());
        ResponseEntity<String> response = restTemplate.postForEntity(url, orderDTO, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new PaymentException(response.getStatusCode().getReasonPhrase());
        }

        return response.getBody();
    }

    public void completeTransaction(Long id, String status) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);

        if (transactionOptional.isEmpty()) {
            throw new NotFoundException(String.format("Transaction with id '%d' not found", id));
        }

        Transaction transaction = transactionOptional.get()
                .toBuilder()
                .completedOn(new Date())
                .done(true)
                .status(determineStatus(status))
                .build();

        transactionRepository.save(transaction);
    }

    public TransactionStatus determineStatus(String status) {
        if (status.equals("success")) {
            return TransactionStatus.COMPLETED;
        }
        return TransactionStatus.FAILED;
    }

    @SneakyThrows
    public static String buildCallbackUrl(Long transactionId, String status) {
        UriComponents context = ServletUriComponentsBuilder.fromCurrentContextPath().build();

        String host = context.getHost();

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(context.getPort())
                .path(String.format(CALLBACK_URL, transactionId, status))
                .build();

        return uriComponents.toUri().toURL().toString();
    }

    private Double getTotal(Invoice invoice) {
        return invoice.getInvoiceItems().stream()
                .reduce(0.0, (partialTotal, invoiceItem) -> partialTotal + (invoiceItem.getPrice() * invoiceItem.getQuantity()), Double::sum);
    }

    private Retailer getRetailer(String retailerName) {
        Optional<Retailer> retailerOptional = retailerRepository.findByName(retailerName);

        if (retailerOptional.isEmpty()) {
            throw new NotFoundException(String.format("Retailer with name '%s' not found", retailerName));
        }
        return retailerOptional.get();
    }

    private InvoiceItem createInvoiceItem(InvoiceItemDTO invoiceItemDTO) {
        Optional<Book> bookOptional = bookRepository.findById(invoiceItemDTO.getId());

        if (bookOptional.isEmpty()) {
            throw new NotFoundException(String.format("Book with id '%d' not found", invoiceItemDTO.getId()));
        }

        Book book = bookOptional.get();

        return InvoiceItem.builder()
                .name(book.getTitle())
                .quantity(invoiceItemDTO.getQuantity())
                .price(book.getPrice())
                .build();
    }
}
