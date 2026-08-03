package com.auction.dutch.model.dto.request;

public record UpdateCategoryRequest(String code, String name, Long parentId) {

}
