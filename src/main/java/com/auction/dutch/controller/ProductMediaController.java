package com.auction.dutch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.DeleteProductMediaRequest;
import com.auction.dutch.model.dto.request.UpdateProductMediaOrderRequest;
import com.auction.dutch.service.ProductMediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductMediaController {

    private final ProductMediaService productMediaService;

    @DeleteMapping("/admin/products/{productId}/media")
    public ResponseEntity<Void> deleteProductMedia(@PathVariable Long productId,
            @Valid @RequestBody DeleteProductMediaRequest request) {
        productMediaService.deleteProductMedia(productId, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/products/{productId}/media/order")
    public ResponseEntity<Void> updateOrder(
            @PathVariable Long productId, @Valid @RequestBody UpdateProductMediaOrderRequest request) {

        productMediaService.updateOrder(productId, request);

        return ResponseEntity.noContent().build();
    }
}
