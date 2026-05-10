package com.studygroup.auth.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class JwtSigningKeys {

    private JwtSigningKeys() {}

    /**
     * HS256 key derived from any secret string. SHA-256 yields 256 bits so short
     * env values (e.g. docker defaults) do not trigger WeakKeyException.
     */
    public static SecretKey hmacSha256FromSecret(String secret) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
