package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.CreateProductRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.ProductDetailResponse;
import com.auction.dutch.model.dto.response.ProductItemResponse;
import com.auction.dutch.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductItemResponse>>> getProductItems() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", productService.getProducts()));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetail(
            @PathVariable(name = "id") Long productId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", productService.getProductDetail(productId)));
    }

    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", productService.createProduct(request)));
    }

    @PatchMapping("/admin/products/{id}/active")
    public ResponseEntity activeProduct(@PathVariable(name = "id") Long productId) {
        productService.activateProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/products/{id}/inactive")
    public ResponseEntity inactiveProduct(@PathVariable(name = "id") Long productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
