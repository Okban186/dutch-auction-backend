package com.auction.dutch.model.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record CategoryTreeResponse(

        Long id,

        String code,

        String name,

        List<CategoryTreeResponse> children) {
}
