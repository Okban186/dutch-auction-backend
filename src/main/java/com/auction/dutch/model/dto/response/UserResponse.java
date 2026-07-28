package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.auction.dutch.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

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

}
