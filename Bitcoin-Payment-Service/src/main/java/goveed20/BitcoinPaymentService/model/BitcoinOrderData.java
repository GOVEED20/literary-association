package goveed20.BitcoinPaymentService.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BitcoinOrderData {
    private Integer id;
    private String status;
    private String price_currency;
    private Double price_amount;
    private String pay_amount;
    private String receive_currency;
    private String receive_amount;
    private String created_at;
    private String expire_at;
    private String payment_address;
    private String order_id;
    private String underpaid_amount;
    private String overpaid_amount;
    private Boolean is_refundable;
    private String payment_url;
}
