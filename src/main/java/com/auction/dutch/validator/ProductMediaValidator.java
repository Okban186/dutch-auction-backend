package com.auction.dutch.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.auction.dutch.enums.MediaType;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.model.dto.internal.DetectedMediaInfo;
import com.auction.dutch.model.dto.internal.UploadRequestItem;
import com.auction.dutch.repository.ProductMediaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMediaValidator {

    private final ProductMediaRepository productMediaRepository;

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024L; // 10MB

    private static final long MAX_VIDEO_SIZE = 10 * 1024 * 1024L; // 10MB

    private static final int MAX_IMAGES_PER_PRODUCT = 20;

    private static final int MAX_VIDEOS_PER_PRODUCT = 3;

    public void validateUploadQuota(
            Long productId,
            List<UploadRequestItem> items) {

        if (items == null || items.isEmpty()) {
            throw new AppException(
                    ErrorCode.INVALID_MEDIA_REQUEST);
        }

        int incomingImages = 0;
        int incomingVideos = 0;

        for (UploadRequestItem item : items) {

            if (item.mediaType() == null) {
                throw new AppException(
                        ErrorCode.INVALID_MEDIA_REQUEST);
            }

            if (item.mediaType() == MediaType.IMAGE) {
                incomingImages++;
            }

            if (item.mediaType() == MediaType.VIDEO) {
                incomingVideos++;
            }
        }

        validateMediaCount(
                productId,
                incomingImages,
                incomingVideos);
    }

    public void validateConfirmedMedia(
            Long productId,
            List<DetectedMediaInfo> mediaList) {

        if (mediaList == null || mediaList.isEmpty()) {
            throw new AppException(
                    ErrorCode.INVALID_MEDIA_REQUEST);
        }

        int incomingImages = 0;
        int incomingVideos = 0;

        for (DetectedMediaInfo media : mediaList) {

            validateMediaType(
                    media.requestedType(),
                    media.actualType());

            validateMediaSize(
                    media.actualType(),
                    media.fileSize());

            if (media.actualType() == MediaType.IMAGE) {
                incomingImages++;
            }

            if (media.actualType() == MediaType.VIDEO) {
                incomingVideos++;
            }
        }

        validateMediaCount(
                productId,
                incomingImages,
                incomingVideos);
    }

    private void validateMediaType(
            MediaType requestedType,
            MediaType actualType) {

        if (requestedType != actualType) {

            throw new AppException(
                    ErrorCode.MEDIA_TYPE_MISMATCH);
        }
    }

    private void validateMediaSize(
            MediaType mediaType,
            long fileSize) {

        if (mediaType == MediaType.IMAGE
                && fileSize > MAX_IMAGE_SIZE) {

            throw new AppException(
                    ErrorCode.MEDIA_FILE_TOO_LARGE);
        }

        if (mediaType == MediaType.VIDEO
                && fileSize > MAX_VIDEO_SIZE) {

            throw new AppException(
                    ErrorCode.MEDIA_FILE_TOO_LARGE);
        }
    }

    private void validateMediaCount(
            Long productId,
            int incomingImages,
            int incomingVideos) {

        long existingImages = productMediaRepository
                .countByProductIdAndMediaType(
                        productId,
                        MediaType.IMAGE);

        long existingVideos = productMediaRepository
                .countByProductIdAndMediaType(
                        productId,
                        MediaType.VIDEO);

        if (existingImages + incomingImages > MAX_IMAGES_PER_PRODUCT) {

            throw new AppException(
                    ErrorCode.PRODUCT_MEDIA_COUNT_EXCEEDED);
        }

        if (existingVideos + incomingVideos > MAX_VIDEOS_PER_PRODUCT) {

            throw new AppException(
                    ErrorCode.PRODUCT_MEDIA_COUNT_EXCEEDED);
        }
    }
}
