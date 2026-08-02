package com.auction.dutch.model.dto.request;

public record CreateCategoryRequest(

        String code,

        String name,

        Long parentId) {
}
