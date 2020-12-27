package goveed20.PaymentConcentrator.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicFeignClientConfig {
    @Bean
    public FeignClientBuilder feignClientBuilder(@Autowired ApplicationContext context) {
        return new FeignClientBuilder(context);
    }
}
