package com.auction.dutch.model.dto.response;

import com.auction.dutch.enums.ProductStatus;

import lombok.Builder;

@Builder
public record ProductDetailResponse(

        Long id,

        String productCode,

        String name,

        String description,

        Long categoryId,

        String categoryName,

        Long brandId,

        String brandName,

        Integer stockQuantity,

        ProductStatus status

) {
}