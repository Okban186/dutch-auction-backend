package com.auction.dutch.config.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auction.dutch.config.properties.StorageProperties;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final StorageProperties storageProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(storageProperties.minio().url())
                .credentials(storageProperties.minio().accessKey(), storageProperties.minio().secretKey())
                .build();
    }
}
