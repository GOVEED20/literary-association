package goveed20.PaypalPaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaypalPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalPaymentServiceApplication.class, args);
	}

}
