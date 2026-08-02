package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.auction.dutch.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(

    Long id,

    String username,

    String displayName,

    String userAddresses,

    String email,

    String phoneNumber,

    BigDecimal walletBalance,

    UserStatus status,

    LocalDateTime createdAt,

    LocalDateTime updatedAt) {

}
