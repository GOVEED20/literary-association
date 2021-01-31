package goveed20.LiteraryAssociationApplication.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "paymentconcentrator")
public class YAMLConfig {

    private String name;
    private String registrationToken;

    public String getName() {
        return name;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}
