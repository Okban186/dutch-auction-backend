package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(
    String accessToken,
    UserResponse userProfile) {
}
