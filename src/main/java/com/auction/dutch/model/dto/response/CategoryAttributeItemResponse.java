package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record CategoryAttributeItemResponse(
        Long id,

        String code,

        String name,

        boolean required,

        Integer displayOrder) {
}
