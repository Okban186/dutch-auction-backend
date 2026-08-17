package com.auction.dutch.config.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "app.media")
public record MediaProperties(
        DataSize maxVideoSize,
        DataSize maxImageSize,
        Integer maxImagePerProduct,
        Integer maxVideoPerProduct,
        Presigned presigned,
        Temp temp) {

    public record Presigned(
            Duration uploadTtl,
            Duration viewTtl) {
    }

    public record Temp(
            Duration bucketSize,
            Duration cleanupInterval) {
    }
}
