package com.auction.dutch.model.dto.response;

import com.auction.dutch.enums.MediaType;

public record ProductMediaOrderResponse(
        Long mediaId,
        MediaType mediaType,
        Integer displayOrder,
        String viewUrl) {
}
