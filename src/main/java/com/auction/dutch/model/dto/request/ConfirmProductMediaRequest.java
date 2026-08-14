package com.auction.dutch.model.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record ConfirmProductMediaRequest(

                @Valid @NotEmpty List<ConfirmMediaItemRequest> items

) {
}
