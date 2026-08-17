package com.auction.dutch.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        String provider,
        Minio minio) {

    public record Minio(
            String url,
            String accessKey,
            String secretKey,
            Bucket bucket) {
    }

    public record Bucket(
            String temp) {
    }
}