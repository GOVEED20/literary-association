package goveed20.PaypalPaymentService.configurations;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client.mode}")
    private String mode;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", mode);

        return sdkConfig;
    }

//    @Bean
//    public OAuthTokenCredential authTokenCredential() {
//        OAuthTokenCredential token = new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
//        return token;
//    }

    @Bean
    public APIContext apiContext() {
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }
}
