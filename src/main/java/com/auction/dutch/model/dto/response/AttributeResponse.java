package com.auction.dutch.model.dto.response;

import com.auction.dutch.enums.AttributeDataType;

public record AttributeResponse(Long id, String code, String name, AttributeDataType dataType) {

}
