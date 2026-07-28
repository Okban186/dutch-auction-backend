package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.auction.dutch.enums.UserStatus;
import com.auction.dutch.model.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponse {
  private Long id;

  private String username;

  private String displayName;

  private String userAddresses;

  private String email;

  private String phoneNumber;

  private BigDecimal walletBalance;
  private UserStatus status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Set<Role> roles;

}
