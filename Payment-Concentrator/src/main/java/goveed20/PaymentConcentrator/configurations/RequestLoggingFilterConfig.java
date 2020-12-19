package goveed20.PaymentConcentrator.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Slf4j
public class RequestLoggingFilterConfig extends AbstractRequestLoggingFilter {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
        logger.info(s);
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
        logger.info(s);
    }
}
