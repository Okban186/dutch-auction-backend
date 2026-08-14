package com.auction.dutch.cache;

public interface UploadTrackingService {

    int getPendingUploads(
            Long sellerId);

    void increasePendingUploads(
            Long sellerId,
            int count);

    void decreasePendingUploads(
            Long sellerId,
            int count);
}
