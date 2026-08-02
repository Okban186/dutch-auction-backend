package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

    @NotBlank(message = "Username is required") String username,

    String userAddresses,

    @NotBlank(message = "DisplayName is required") String displayName,

    @NotBlank(message = "Email is required") String email,

    String phoneNumber,

    @NotBlank(message = "Password is required") String password) {

}
