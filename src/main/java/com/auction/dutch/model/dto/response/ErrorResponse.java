package com.auction.dutch.model.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String message) {
}
