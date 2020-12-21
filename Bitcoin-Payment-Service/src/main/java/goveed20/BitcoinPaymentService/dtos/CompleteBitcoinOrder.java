package goveed20.BitcoinPaymentService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CompleteBitcoinOrder {

    private Integer id;
    private String order_id;
    private String status;
    private Double price_amount;
    private String price_currency;
    private String receive_currency;
    private Double receive_amount;
    private Double pay_amount;
    private String pay_currency;
    private String created_at;

}
