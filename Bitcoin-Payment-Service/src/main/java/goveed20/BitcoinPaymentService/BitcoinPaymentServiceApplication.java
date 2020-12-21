package goveed20.BitcoinPaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"goveed20.PaymentConcentrator.payment.concentrator.plugin"})
public class BitcoinPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinPaymentServiceApplication.class, args);
	}

}
