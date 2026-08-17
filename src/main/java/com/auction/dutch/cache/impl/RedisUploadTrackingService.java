package com.auction.dutch.cache.impl;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.auction.dutch.cache.UploadTrackingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisUploadTrackingService
                implements UploadTrackingService {

        private static final String KEY_PREFIX = "media:pending:";

        private static final Duration TTL = Duration.ofHours(1);

        private final StringRedisTemplate redisTemplate;

        @Override
        public int getPendingUploads(
                        Long sellerId) {

                String value = redisTemplate.opsForValue()
                                .get(buildKey(sellerId));

                return value == null
                                ? 0
                                : Integer.parseInt(value);
        }

        @Override
        public void increasePendingUploads(
                        Long sellerId,
                        int count) {

                String key = buildKey(sellerId);

                Long result = redisTemplate.opsForValue()
                                .increment(key, count);

                if (result != null && result == count) {
                        redisTemplate.expire(key, TTL);
                }
        }

        @Override
        public void decreasePendingUploads(
                        Long sellerId,
                        int count) {

                String key = buildKey(sellerId);

                Long current = redisTemplate.opsForValue()
                                .decrement(key, count);

                if (current != null
                                && current <= 0) {

                        redisTemplate.delete(key);
                }
        }

        private String buildKey(Long sellerId) {

                return KEY_PREFIX + sellerId;
        }

        private static final Duration UPLOAD_SESSION_TTL = Duration.ofMinutes(3);

        public void saveUploadSession(Long sellerId, Long productId, String storageKey) {
                String key = storageKey;

                String value = sellerId + ":" + productId + ":" + storageKey;

                redisTemplate.opsForValue().set(
                                key,
                                value,
                                UPLOAD_SESSION_TTL);
        }

        public boolean existsUploadSession(String storageKey) {
                return Boolean.TRUE.equals(redisTemplate.hasKey(storageKey));
        }
}
