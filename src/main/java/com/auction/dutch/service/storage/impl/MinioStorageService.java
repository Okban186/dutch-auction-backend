package com.auction.dutch.service.storage.impl;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.model.dto.internal.StorageObjectInfo;
import com.auction.dutch.service.storage.StorageService;

import io.minio.CopyObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioStorageService
        implements StorageService {

    private final MinioClient minioClient;

    @Override
    public String generatePresignedUploadUrl(
            String bucket,
            String objectKey,
            Duration expiry) {

        try {

            return minioClient
                    .getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs
                                    .builder()
                                    .method(Method.PUT)
                                    .bucket(bucket)
                                    .object(objectKey)
                                    .expiry(Math.toIntExact(expiry.getSeconds()))
                                    .build());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot generate presigned url",
                    e);
        }
    }

    @Override
    public void moveObject(
            String bucket,
            String sourceKey,
            String targetKey) {

        try {

            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(targetKey)
                            .source(
                                    io.minio.CopySource
                                            .builder()
                                            .bucket(bucket)
                                            .object(sourceKey)
                                            .build())
                            .build());

            deleteObject(
                    bucket,
                    sourceKey);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot move object",
                    e);
        }
    }

    @Override
    public void deleteObject(
            String bucket,
            String objectKey) {

        try {

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot delete object",
                    e);
        }
    }

    @Override
    public InputStream getObject(
            String bucket,
            String objectKey) {

        try {

            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot get object",
                    e);
        }
    }

    @Override
    public long getObjectSize(
            String bucket,
            String objectKey) {

        try {

            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build())
                    .size();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot stat object",
                    e);
        }
    }

    @Override
    public String generatePresignedViewUrl(
            String bucket,
            String objectKey,
            Duration expiry) {
        try {

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(Math.toIntExact(expiry.getSeconds()))
                            .build());

        } catch (Exception e) {

            throw new AppException(
                    ErrorCode.STORAGE_ERROR);
        }
    }

    @Override
    public List<StorageObjectInfo> listObjects(
            String bucket,
            String prefix) {

        try {

            List<StorageObjectInfo> objects = new ArrayList<>();

            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(prefix)
                            .recursive(true)
                            .build());

            for (Result<Item> result : results) {

                Item item = result.get();

                objects.add(
                        new StorageObjectInfo(
                                item.objectName()));
            }

            return objects;

        } catch (Exception e) {

            throw new AppException(
                    ErrorCode.STORAGE_ERROR);
        }
    }
}