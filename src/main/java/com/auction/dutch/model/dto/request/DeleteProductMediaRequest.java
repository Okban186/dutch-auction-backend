package com.auction.dutch.model.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record DeleteProductMediaRequest(@NotEmpty List<Long> mediaIds) {

}
