package com.auction.dutch.model.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
    String accessToken,
    String refeshToken,
    UserResponse userProfile) {
}
