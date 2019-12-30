package ninja.cero.store;

import ninja.cero.store.accesslog.AccessLoggingGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }

    @Value("${ui.url}")
    private String uiURL;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AccessLoggingGatewayFilterFactory accessLoggingFilterFactory) {
        System.out.println("★★★★★★" + uiURL);
        return builder.routes()
            .route(r -> r.path("/api/**")
                .filters(f -> f
                    .filter(accessLoggingFilterFactory.apply(c -> {
                    }))
                    .rewritePath("/api/(?<path>.*)", "/${path}"))
                .uri("lb://store-web")
            )
            .route(r -> (r.path("/**"))
                .filters(f -> f
                    .filter(accessLoggingFilterFactory.apply(c -> {
                    }))
                    .rewritePath("/(?<path>.*)", "/${path}"))
                .uri(uiURL)
            )
            .build();
    }
}
