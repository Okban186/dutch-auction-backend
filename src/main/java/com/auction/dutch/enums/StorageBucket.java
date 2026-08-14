package com.auction.dutch.enums;

public enum StorageBucket {

    PRODUCT_MEDIA("productmedia");

    private final String bucketName;

    StorageBucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
