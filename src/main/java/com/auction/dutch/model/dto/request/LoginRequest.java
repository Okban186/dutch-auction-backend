package com.auction.dutch.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "Username is required") String username,

    @NotBlank(message = "password is required") String password) {
}
