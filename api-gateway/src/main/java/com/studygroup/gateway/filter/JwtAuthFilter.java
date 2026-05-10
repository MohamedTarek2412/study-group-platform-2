package com.studygroup.gateway.filter;

import com.studygroup.gateway.security.JwtSigningKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.regex.Pattern;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered, InitializingBean {

    private static final Pattern UUID_PATH_SEGMENT = Pattern.compile(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    @Value("${jwt.secret:this-is-a-very-secure-jwt-secret-key-that-is-at-least-256-bits-long-for-security-requirements}")
    private String jwtSecret;

    private SecretKey signingKey;

    @Override
    public void afterPropertiesSet() {
        this.signingKey = JwtSigningKeys.hmacSha256FromSecret(jwtSecret);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod() != null ? request.getMethod().toString() : "";

        if ("OPTIONS".equals(method)) {
            return chain.filter(exchange);
        }

        if (isPublicEndpoint(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (!authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);
            if (userId == null || userId.isBlank()) {
                userId = claims.getSubject();
            }
            String role = claims.get("role", String.class);

            ServerHttpRequest.Builder rb = request.mutate().header("X-User-Id", userId);
            if (role != null && !role.isBlank()) {
                rb.header("X-User-Role", role);
            }
            ServerHttpRequest modifiedRequest = rb.build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicEndpoint(String path, String method) {
        if (path.equals("/api/auth/register") || path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")) {
            return true;
        }

        if ("GET".equals(method)) {
            if (path.equals("/api/groups") || path.equals("/api/groups/search")) {
                return true;
            }

            if (isSingleResourcePath(path, "/api/groups/")
                    || isSingleResourcePath(path, "/api/users/")) {
                return true;
            }
        }

        return path.startsWith("/actuator");
    }

    private boolean isSingleResourcePath(String path, String prefix) {
        if (!path.startsWith(prefix)) {
            return false;
        }

        String id = path.substring(prefix.length());
        return UUID_PATH_SEGMENT.matcher(id).matches();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
