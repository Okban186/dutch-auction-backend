package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MediaOrderUpdate(

        @NotNull Long mediaId,

        @NotNull @Positive Integer displayOrder) {
}
