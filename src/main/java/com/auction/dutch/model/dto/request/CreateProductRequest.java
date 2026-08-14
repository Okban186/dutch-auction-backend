package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(

        @NotBlank String name,

        String description,

        @NotNull Long categoryId,

        Long brandId,

        @NotNull @Positive Integer stockQuantity

) {
}
