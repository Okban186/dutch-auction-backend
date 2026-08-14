package com.auction.dutch.model.dto.request;

import com.auction.dutch.enums.MediaType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ConfirmMediaItemRequest(

        @NotNull String storageKey,

        @NotNull MediaType mediaType,

        @NotNull @Positive Integer displayOrder

) {
}