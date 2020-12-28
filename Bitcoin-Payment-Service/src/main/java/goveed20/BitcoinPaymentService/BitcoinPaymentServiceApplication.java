package goveed20.BitcoinPaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"goveed20.PaymentConcentrator.payment.concentrator.plugin", "goveed20.BitcoinPaymentService"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"goveed20.PaymentConcentrator.payment.concentrator.plugin"})
@EnableAsync
public class BitcoinPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinPaymentServiceApplication.class, args);
	}

}
