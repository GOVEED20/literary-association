package goveed20.LiteraryAssociationApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LiteraryAssociationApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraryAssociationApplication.class, args);
	}

}
