package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoryAttributeUpdateRequest(
        @NotNull Long attributeId,

        @NotNull Boolean required,

        @NotNull Integer displayOrder) {

}
