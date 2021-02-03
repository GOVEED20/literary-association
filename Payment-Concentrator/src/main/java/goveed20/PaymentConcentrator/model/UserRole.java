package goveed20.PaymentConcentrator.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    APPLICATION;

    @Override
    public String getAuthority() {
        return name();
    }
}
