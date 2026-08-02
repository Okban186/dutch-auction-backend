package com.auction.dutch.model.dto.request;

public record UpdateProfileRequest(
    String displayName,

    String userAddresses,

    String phoneNumber) {
}
