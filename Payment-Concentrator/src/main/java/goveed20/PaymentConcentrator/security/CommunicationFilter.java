package goveed20.PaymentConcentrator.security;

import goveed20.PaymentConcentrator.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CommunicationFilter extends OncePerRequestFilter {

    @Autowired
    private RetailerRepository retailerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURL = httpServletRequest.getRequestURI();
        if (requestURL.equals("/api/actuator/health")) {
            return;
        } else if (requestURL.startsWith("/api/h2")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            String authToken = httpServletRequest.getHeader("Authorization");
            if (retailerRepository.findByRegistrationToken(authToken).isEmpty()) {
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
