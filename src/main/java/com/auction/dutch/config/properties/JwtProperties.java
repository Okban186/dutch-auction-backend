package com.auction.dutch.config.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String algorithm,
        String macAlgorithm,
        String secret,
        Duration accessTokenTtl,
        Duration refreshTokenTtl) {
}
