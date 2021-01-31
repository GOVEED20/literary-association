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
        System.out.println("Addr  " + httpServletRequest.getRemoteAddr());
        System.out.println("ServerName  " + httpServletRequest.getServerName());
        String requestURL = httpServletRequest.getRequestURI();
        if (requestURL.equals("/api/actuator/health")) {
            return;
        } else if (requestURL.startsWith("/api/h2")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else if (httpServletRequest.getHeader("Authorization") != null &&
                httpServletRequest.getHeader("Authorization").matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")){
            String authToken = httpServletRequest.getHeader("Authorization");
            if (retailerRepository.findByRegistrationToken(authToken).isEmpty()) {
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
