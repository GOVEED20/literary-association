package goveed20.PaymentConcentrator.security;

import goveed20.PaymentConcentrator.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
@Setter
public class ApiKeyBasedAuthentication extends AbstractAuthenticationToken {
    private String apiKey;

    public ApiKeyBasedAuthentication(String apiKey) {
        super(Collections.singleton(UserRole.APPLICATION));
        this.apiKey = apiKey;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
