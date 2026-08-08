package com.auction.dutch.model.dto.request;

import com.auction.dutch.enums.AttributeDataType;

public record CreateAttributeRequest(String code, String name, AttributeDataType dataType) {

}
