package goveed20.PaymentConcentrator.services;

import goveed20.PaymentConcentrator.dtos.LoginData;
import goveed20.PaymentConcentrator.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public String login(LoginData loginData) {
        String encodedPass = passwordEncoder.encode(loginData.getPassword());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginData.getUsername(),
                        loginData.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenService.generateToken(authentication);
    }
}
