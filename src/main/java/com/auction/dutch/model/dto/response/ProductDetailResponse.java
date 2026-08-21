package com.auction.dutch.model.dto.response;

import java.util.List;

import com.auction.dutch.enums.ProductStatus;

import lombok.Builder;

@Builder
public record ProductDetailResponse(

                Long id,

                String code,

                String name,

                String description,

                Long categoryId,

                String categoryName,

                Long brandId,

                String brandName,

                Integer stockQuantity,

                ProductStatus status,
                List<ProductMediaResponse> productMediaResponses

) {
}