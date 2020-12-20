package goveed20.BitcoinPaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BitcoinPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinPaymentServiceApplication.class, args);
	}

}
