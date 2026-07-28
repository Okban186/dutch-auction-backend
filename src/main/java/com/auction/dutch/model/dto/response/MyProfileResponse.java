package com.auction.dutch.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileResponse {
  private Long id;

  private String username;

  private String displayName;

  private String userAddresses;

  private String email;

  private String phoneNumber;

  private BigDecimal walletBalance;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
