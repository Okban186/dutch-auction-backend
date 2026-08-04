package com.auction.dutch.model.dto.request;

import java.util.List;

public record UpdateCategoryAttributesRequest(List<CategoryAttributeUpdateRequest> attributes) {

}
