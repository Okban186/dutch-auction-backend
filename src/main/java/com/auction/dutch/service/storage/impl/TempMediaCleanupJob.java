package com.auction.dutch.service.storage.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.auction.dutch.config.properties.MediaProperties;
import com.auction.dutch.enums.StorageBucket;
import com.auction.dutch.model.dto.internal.StorageObjectInfo;
import com.auction.dutch.service.storage.StorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TempMediaCleanupJob {

        private final StorageService storageService;

        private final MediaProperties mediaProperties;

        @Scheduled(cron = "0 */16 * * * *", zone = "UTC")
        public void cleanup() {

                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

                LocalDateTime target = now.minusMinutes(mediaProperties.temp().cleanupInterval().toMinutes());

                long minuteBucket = ((target.getMinute() / mediaProperties.temp().bucketSize().toMinutes())
                                * mediaProperties.temp().bucketSize().toMinutes());

                String prefix = String.format(
                                "temp/%04d/%02d/%02d/%02d/%02d/",
                                target.getYear(),
                                target.getMonthValue(),
                                target.getDayOfMonth(),
                                target.getHour(),
                                minuteBucket);

                String bucket = StorageBucket.PRODUCT_MEDIA
                                .getBucketName();

                List<StorageObjectInfo> objects = storageService.listObjects(
                                bucket,
                                prefix);

                for (StorageObjectInfo object : objects) {

                        storageService.deleteObject(
                                        bucket,
                                        object.objectKey());
                }
        }
}
