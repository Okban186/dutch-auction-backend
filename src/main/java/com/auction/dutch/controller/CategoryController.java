package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.CreateCategoryRequest;
import com.auction.dutch.model.dto.request.UpdateCategoryRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.model.dto.response.CategoryDetailResponse;
import com.auction.dutch.model.dto.response.CategorySummaryResponse;
import com.auction.dutch.model.dto.response.CategoryTreeResponse;
import com.auction.dutch.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // admin

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/admin/categories")
    public ResponseEntity createCategory(@RequestBody CreateCategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryDetailResponse>> updateCategory(@PathVariable Long id,
            @RequestBody UpdateCategoryRequest categoryRequest) {
        return ResponseEntity
                .ok(new ApiResponse<>(200, "Successfull", categoryService.updateCategory(id, categoryRequest)));
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        categoryService.solfDelete(id);
        return ResponseEntity.noContent().build();
    }

    // public

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategorySummaryResponse>>> getCategorySummaryList() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", categoryService.getCategorySummary()));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryDetailResponse>> getCategoryDetail(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", categoryService.getCategoryDetail(id)));
    }

    @GetMapping("/categories/tree")
    public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> getCategoryTree() {
        return ResponseEntity.ok(new ApiResponse<>(200, "", categoryService.getCategoryTree()));
    }

}