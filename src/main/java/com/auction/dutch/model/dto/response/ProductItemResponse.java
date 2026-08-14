package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record ProductItemResponse(

        Long id,

        String productCode,

        String name,

        Integer stockQuantity

) {
}
