package goveed20.BitcoinPaymentService.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BitcoinOrder {

    private Integer id;
    private String order_id;
    private String status;
    private Double price_amount;
    private String price_currency;
    private String receive_currency;
    private String title;
    private String description;
    private String callback_url;
    private String cancel_url;
    private String success_url;
    private String payment_url;
    private String token;

}
