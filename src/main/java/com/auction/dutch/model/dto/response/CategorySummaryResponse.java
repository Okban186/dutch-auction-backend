package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record CategorySummaryResponse(
        Long id,

        String code,

        String name) {

}
