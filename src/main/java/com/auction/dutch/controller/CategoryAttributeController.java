package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.UpdateCategoryAttributesRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.service.CategoryAttributeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryAttributeController {

    private final CategoryAttributeService cAttributeService;

    @GetMapping("/categories/{id}/resolved-attributes")
    public ResponseEntity<ApiResponse<List<CategoryAttributeItemResponse>>> getResolvedAttributes(
            @PathVariable(name = "id") Long categoryId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "", cAttributeService.getResolvedAttributes(categoryId)));
    }

    @GetMapping("/admin/categories/{id}/own-attributes")
    public ResponseEntity<ApiResponse<List<CategoryAttributeItemResponse>>> getOwnAttributes(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "", cAttributeService.getOwnAttributes(id)));
    }

    @PutMapping("/admin/categories/{id}/attributes")
    public ResponseEntity<ApiResponse<List<CategoryAttributeItemResponse>>> updateCategoryAttributes(
            @PathVariable Long id, @RequestBody UpdateCategoryAttributesRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Update successfull", cAttributeService.updateCategoryAttributes(id, request)));
    }
}
