package com.auction.dutch.model.dto.internal;

import lombok.Builder;

@Builder
public record CategoryBreadcrumbDto(

        Long id,

        String code,

        String name) {
}
