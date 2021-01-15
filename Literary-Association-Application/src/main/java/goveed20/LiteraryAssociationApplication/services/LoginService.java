package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.LoginDTO;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.SecurityContext;

@Service
public class LoginService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(LoginDTO loginData) {
        final Authentication authentication;

        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getUsername(),
                loginData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
