package com.auction.dutch.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auction.dutch.cache.UploadTrackingService;
import com.auction.dutch.config.properties.MediaProperties;
import com.auction.dutch.enums.ProductStatus;
import com.auction.dutch.enums.StorageBucket;
import com.auction.dutch.enums.UserStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.model.dto.request.GenerateUploadUrlsRequest;
import com.auction.dutch.model.dto.response.GenerateUploadUrlsResponse;
import com.auction.dutch.model.dto.response.UploadUrlItemResponse;
import com.auction.dutch.model.entity.Product;
import com.auction.dutch.model.entity.User;
import com.auction.dutch.repository.ProductRepository;
import com.auction.dutch.repository.UserRepository;
import com.auction.dutch.service.storage.StorageService;
import com.auction.dutch.validator.ProductMediaValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaUploadService {

        private static final int MAX_PENDING_UPLOADS = 50;

        private final StorageService storageService;

        private final UploadTrackingService uploadTrackingService;

        private final ProductMediaValidator productMediaValidator;

        private final UserRepository userRepository;

        private final ProductRepository productRepository;

        private final MediaProperties mediaProperties;

        public GenerateUploadUrlsResponse generateUploadUrls(

                        Long sellerId,

                        Long productId,

                        GenerateUploadUrlsRequest request) {

                User user = userRepository.findById(sellerId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                if (product.getStatus() == ProductStatus.DELETED)
                        throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

                if (user.getStatus() == UserStatus.DELETED || user.getStatus() == UserStatus.BLOCKED)
                        throw new AppException(ErrorCode.USER_NOT_FOUND);
                if (product.getStatus() == ProductStatus.DELETED || product.getStatus() == ProductStatus.INACTIVE)
                        throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

                validateRequest(request);

                validatePendingUploads(
                                sellerId,
                                request.items().size());

                productMediaValidator.validateUploadQuota(
                                productId,
                                request.items());

                List<UploadUrlItemResponse> items = new ArrayList<>();

                for (int i = 0; i < request.items().size(); i++) {

                        String storageKey = generateTempStorageKey(productId);

                        String uploadUrl = storageService
                                        .generatePresignedUploadUrl(
                                                        StorageBucket.PRODUCT_MEDIA.getBucketName(),
                                                        storageKey,
                                                        mediaProperties.presigned().uploadTtl());
                        items.add(UploadUrlItemResponse.builder()
                                        .storageKey(storageKey)
                                        .uploadUrl(uploadUrl)
                                        .build());
                }

                uploadTrackingService.increasePendingUploads(
                                sellerId,
                                request.items().size());

                return new GenerateUploadUrlsResponse(
                                items);
        }

        private void validateRequest(
                        GenerateUploadUrlsRequest request) {

                if (request == null
                                || request.items() == null
                                || request.items().isEmpty()) {

                        throw new AppException(
                                        ErrorCode.INVALID_MEDIA_REQUEST);
                }
        }

        private void validatePendingUploads(
                        Long sellerId,
                        int requestedCount) {

                int pending = uploadTrackingService
                                .getPendingUploads(
                                                sellerId);

                if (pending + requestedCount > MAX_PENDING_UPLOADS) {

                        throw new AppException(
                                        ErrorCode.TOO_MANY_PENDING_UPLOADS);
                }
        }

        private String generateTempStorageKey(Long productId) {

                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

                long minuteBucket = ((now.getMinute() / mediaProperties.temp().bucketSize().toMinutes())
                                * mediaProperties.temp().bucketSize().toMinutes());

                return String.format(
                                "temp/%04d/%02d/%02d/%02d/%02d/%d/%s",
                                now.getYear(),
                                now.getMonthValue(),
                                now.getDayOfMonth(),
                                now.getHour(),
                                minuteBucket,
                                productId,
                                UUID.randomUUID()
                                                .toString()
                                                .replace("-", ""));
        }
}
