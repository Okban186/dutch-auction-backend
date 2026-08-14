package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(

        String name,

        String description,

        Long categoryId,

        Long brandId,

        @Positive Integer stockQuantity

) {
}
