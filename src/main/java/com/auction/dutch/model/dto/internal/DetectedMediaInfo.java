package com.auction.dutch.model.dto.internal;

import com.auction.dutch.enums.MediaType;

public record DetectedMediaInfo(

        String storageKey,

        MediaType requestedType,

        MediaType actualType,

        String mimeType,

        Integer displayOrder,

        long fileSize) {
}
