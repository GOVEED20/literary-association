package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return baseUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' not found", username)));
    }
}
