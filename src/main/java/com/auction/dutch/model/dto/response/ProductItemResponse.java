package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record ProductItemResponse(

        Long id,

        String code,

        String name,

        Integer stockQuantity,

        String viewUrl

) {
}
