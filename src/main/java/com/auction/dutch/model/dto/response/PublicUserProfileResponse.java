package com.auction.dutch.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUserProfileResponse {
  private Long id;

  private String username;

  private String displayName;

  private String userAddresses;

  private String email;

  private String phoneNumber;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

}
