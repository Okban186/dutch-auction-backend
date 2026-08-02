package com.auction.dutch.model.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PublicUserProfileResponse(
    Long id,

    String username,

    String displayName,

    String userAddresses,

    String email,

    String phoneNumber,

    LocalDateTime createdAt,

    LocalDateTime updatedAt) {

}
