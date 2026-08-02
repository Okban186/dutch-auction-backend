package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder

public record MyProfileResponse(
    Long id,

    String username,

    String displayName,

    String userAddresses,

    String email,

    String phoneNumber,

    BigDecimal walletBalance,

    LocalDateTime createdAt,

    LocalDateTime updatedAt) {

}
