package goveed20.PaymentConcentrator.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenBasedAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final UserDetails principle;

    public TokenBasedAuthentication(String token, UserDetails principle) {
        super(principle.getAuthorities());
        this.token = token;
        this.principle = principle;
    }

    @Override
    public Object getCredentials() {
        return principle.getAuthorities();
    }

    @Override
    public Object getPrincipal() {
        return principle;
    }
}
