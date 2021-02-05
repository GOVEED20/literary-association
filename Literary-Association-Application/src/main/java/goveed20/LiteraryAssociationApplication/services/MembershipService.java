package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.configs.YAMLConfig;
import goveed20.LiteraryAssociationApplication.dtos.MembershipInvoiceDTO;
import goveed20.LiteraryAssociationApplication.dtos.MembershipPreviewDTO;
import goveed20.LiteraryAssociationApplication.dtos.OrderDTO;
import goveed20.LiteraryAssociationApplication.dtos.PaymentFieldsDTO;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.exceptions.PaymentException;
import goveed20.LiteraryAssociationApplication.model.MembershipTransaction;
import goveed20.LiteraryAssociationApplication.model.Retailer;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.repositories.MembershipTransactionRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.utils.RestTemplateResponseErrorHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
public class MembershipService {
    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private MembershipTransactionRepository membershipTransactionRepository;

    @Autowired
    private WriterRepository writerRepository;

    private final RestTemplate restTemplate = new RestTemplateBuilder().errorHandler(new RestTemplateResponseErrorHandler()).build();

    private static final String CALLBACK_URL = "/api/transaction/%d/%s";

    @Autowired
    private YAMLConfig myConfig;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", myConfig.getRegistrationToken());

        return headers;
    }

    public MembershipPreviewDTO getActiveMembership(String username) {
        return writerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("Writer with username '%s' not found", username)))
                .getTransactions().stream()
                .filter(t -> t instanceof MembershipTransaction && t.getStatus().equals(TransactionStatus.CREATED))
                .map(t -> new MembershipPreviewDTO(t.getId(), t.getTotal(), t.getInvoice().getRetailer().getName()))
                .findFirst().orElse(null);
    }

    public String initializeMembershipTransaction(MembershipInvoiceDTO invoiceDTO) {
        Retailer retailer = retailerRepository.findByName(myConfig.getName())
                .orElseThrow(() -> new PaymentException("Service unavailable"));

        MembershipTransaction membershipTransaction = membershipTransactionRepository.findById(invoiceDTO.getMembershipTransactionId())
                .orElseThrow(() -> new PaymentException("Invalid membership transaction id"));

        OrderDTO orderDTO = OrderDTO.builder()
                .amount(10.0)
                .errorURL(buildCallbackUrl(membershipTransaction.getId(), "error"))
                .failedURL(buildCallbackUrl(membershipTransaction.getId(), "failed"))
                .successURL(buildCallbackUrl(membershipTransaction.getId(), "success"))
                .retailer(retailer.getName())
                .transactionId(membershipTransaction.getId())
                .paymentFields(PaymentFieldsDTO.builder().subscription(invoiceDTO.getSubscription()).name("LA membership transaction").build())
                .build();

        String url = String.format("http://localhost:8080/api/payment-services/%s/initialize-payment", invoiceDTO.getPaymentMethod());
        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(orderDTO, getHeaders()), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new PaymentException(response.getStatusCode().getReasonPhrase());
        }

        membershipTransaction.setInitializedOn(new Date());
        membershipTransaction.setMonths(invoiceDTO.getSubscription() ? 0L : 1L);
        membershipTransactionRepository.save(membershipTransaction);

        return response.getBody();
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
}
