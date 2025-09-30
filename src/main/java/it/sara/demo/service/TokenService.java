package it.sara.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final String issuer;
    private final long ttlSeconds;

    public TokenService(JwtEncoder encoder,
                        @Value("${jwt.issuer}") String issuer,
                        @Value("${jwt.ttl-seconds}") long ttlSeconds) {
        this.encoder = encoder;
        this.issuer = issuer;
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ttlSeconds))
                .subject(subject)
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}