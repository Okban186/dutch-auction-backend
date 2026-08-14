package com.auction.dutch.model.dto.response;

import java.util.List;

public record BatchReorderProductMediaResponse(
        List<ProductMediaOrderResponse> updated) {
}
