package com.auction.dutch.model.dto.response;

import com.auction.dutch.enums.MediaType;

import lombok.Builder;

@Builder
public record ProductMediaResponse(

        Long id,
        String mediaUrl,
        MediaType mediaType,
        Integer displayOrder) {

}
