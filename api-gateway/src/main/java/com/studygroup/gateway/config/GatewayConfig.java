package com.studygroup.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://auth-service:8081"))
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://user-service:8082"))
                .route("group-service", r -> r.path("/api/groups/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://group-service:8083"))
                .route("discussion-service", r -> r.path("/api/posts/**", "/api/materials/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://discussion-service:8084"))
                .route("discussion-groups", r -> r.path("/api/groups/{id}/posts/**", "/api/groups/{id}/materials/**")
                        .filters(f -> f.rewritePath("/api/groups/(?<groupId>[^/]+)/.*", "/api/groups/${groupId}/$\\{segment\\}"))
                        .uri("http://discussion-service:8084"))
                .build();
    }
}
