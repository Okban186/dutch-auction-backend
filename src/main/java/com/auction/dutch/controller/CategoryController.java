package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.model.dto.response.CategoryDetailResponse;
import com.auction.dutch.model.dto.response.CategorySummaryResponse;
import com.auction.dutch.model.dto.response.CategoryTreeResponse;
import com.auction.dutch.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<CategorySummaryResponse>>> getCategorySummaryList() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", categoryService.getCategorySummary()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDetailResponse>> getCategoryDetail(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", categoryService.getCategoryDetail(id)));
    }

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> getCategoryTree() {
        return ResponseEntity.ok(new ApiResponse<>(200, "", categoryService.getCategoryTree()));
    }

    @GetMapping("/{id}/attributes")
    public ResponseEntity<ApiResponse<List<CategoryAttributeItemResponse>>> getCategoryAttributesItems(
            @PathVariable(name = "id") Long categoryId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "", categoryService.getCategoryAttributeItem(categoryId)));
    }
}
