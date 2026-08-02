package com.auction.dutch.model.dto.response;

import java.util.List;

import com.auction.dutch.model.dto.internal.CategoryBreadcrumbDto;

import lombok.Builder;

@Builder
public record CategoryDetailResponse(

        Long id,

        String code,

        String name,

        List<CategoryBreadcrumbDto> cBreadcrumbDtos) {
}
