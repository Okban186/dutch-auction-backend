package com.auction.dutch.enums;

import java.util.Set;

public enum MediaType {

    IMAGE(Set.of(
            "image/jpeg",
            "image/png",
            "image/webp")),

    VIDEO(Set.of(
            "video/mp4", "video/quicktime"));

    private final Set<String> mimeTypes;

    MediaType(Set<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public boolean matches(String mimeType) {
        return mimeTypes.contains(mimeType);
    }
}