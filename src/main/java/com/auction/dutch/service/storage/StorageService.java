package com.auction.dutch.service.storage;

import java.io.InputStream;
import java.util.List;

import com.auction.dutch.model.dto.internal.StorageObjectInfo;

public interface StorageService {

    String generatePresignedUploadUrl(
            String bucket,
            String objectKey,
            int expiryMinutes);

    void moveObject(
            String bucket,
            String sourceKey,
            String targetKey);

    void deleteObject(
            String bucket,
            String objectKey);

    InputStream getObject(
            String bucket,
            String objectKey);

    long getObjectSize(
            String bucket,
            String objectKey);

    String generatePresignedViewUrl(
            String bucket,
            String objectKey,
            int expiryMinutes);

    List<StorageObjectInfo> listObjects(
            String bucket,
            String prefix);
}
