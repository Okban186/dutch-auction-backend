package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "Username is required")
  private String username;

  private String userAddresses;

  @NotBlank(message = "DisplayName is required")
  private String displayName;

  @NotBlank(message = "Email is required")
  private String email;

  private String phoneNumber;

  @NotBlank(message = "Password is required")
  private String password;

}
