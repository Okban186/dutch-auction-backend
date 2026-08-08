package com.auction.dutch.model.dto.request;

import com.auction.dutch.enums.AttributeDataType;

public record UpdatePartialAttributeRequest(String code, String name, AttributeDataType dataType) {

}
