package com.studygroup.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${studygroup.gateway.discussion-uri:http://discussion-service:8084}")
    private String discussionUri;

    @Value("${studygroup.gateway.auth-uri:http://auth-service:8081}")
    private String authUri;

    @Value("${studygroup.gateway.user-uri:http://user-service:8082}")
    private String userUri;

    @Value("${studygroup.gateway.group-uri:http://group-service:8083}")
    private String groupUri;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // Order matters: nested group paths must match before the generic /api/groups/** route.
        return builder.routes()
                .route("discussion-service", r -> r.path("/api/groups/*/posts/**", "/api/groups/*/materials/**")
                        .uri(discussionUri))
                .route("auth-public-service", r -> r.path("/api/auth/register", "/api/auth/login")
                        .filters(f -> f.removeRequestHeader("Authorization"))
                        .uri(authUri))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri(authUri))
                .route("user-service", r -> r.path("/api/users/**")
                        .uri(userUri))
                .route("group-service", r -> r.path("/api/groups/**")
                        .uri(groupUri))
                .build();
    }
}
