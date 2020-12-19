package goveed20.Gateway.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Autowired
    private PaymentServiceFilter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("payment-concentrator", r -> r.path("/api/**")
                        .filters(f -> f.rewritePath("/payment-concentrator/(?<path>.*)", "/$\\{path}"))
                        .uri("lb://payment-concentrator"))
                .route("payment-service", r -> r.path("/{serviceName}/api/payment/**")
                        .filters(f -> f.filter(filter))
                        .uri("no://op"))
                .build();
    }

}
