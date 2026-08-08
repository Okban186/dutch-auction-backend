package com.auction.dutch.model.dto.response;

import java.time.LocalDateTime;

import com.auction.dutch.enums.AttributeDataType;
import com.auction.dutch.enums.AttributeStatus;

public record AttributeAdminResponse(Long id, String code, String name, AttributeStatus status,
                AttributeDataType dataType,
                LocalDateTime createdAt) {

}
