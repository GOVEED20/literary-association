package goveed20.PaypalPaymentService;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableFeignClients(basePackages = {"goveed20.PaymentConcentrator.payment.concentrator.plugin"})
@EnableAsync
public class PaypalPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalPaymentServiceApplication.class, args);
	}

}
