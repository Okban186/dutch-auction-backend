package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record UploadUrlItemResponse(

        String storageKey,

        String uploadUrl

) {
}
