package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.auction.dutch.enums.UserStatus;
import com.auction.dutch.model.entity.Role;

public record AdminUserResponse(
    Long id,

    String username,

    String displayName,

    String userAddresses,

    String email,

    String phoneNumber,

    BigDecimal walletBalance,
    UserStatus status,

    LocalDateTime createdAt,

    LocalDateTime updatedAt,

    Set<Role> roles) {

}
