package com.studygroup.auth.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@Profile("local")
public class LocalUserProfileBootstrap {

    private static final Logger log = LoggerFactory.getLogger(LocalUserProfileBootstrap.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${studygroup.user-service-url:http://localhost:8082}")
    private String userServiceUrl;

    public void sync(UUID userId, String email, String requestedRole) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var body = new BootstrapBody(userId, email, requestedRole);
            restTemplate.postForEntity(
                    userServiceUrl + "/api/users/bootstrap",
                    new HttpEntity<>(body, headers),
                    Void.class);
        } catch (Exception e) {
            log.warn("Could not create user profile via user-service (start user-service before auth, or ignore): {}", e.getMessage());
        }
    }

    public record BootstrapBody(UUID userId, String email, String requestedRole) {}
}
