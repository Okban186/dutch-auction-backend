package com.auction.dutch.model.dto.internal;

import com.auction.dutch.enums.MediaType;

import jakarta.validation.constraints.NotNull;

public record UploadRequestItem(

                @NotNull MediaType mediaType

) {
}
