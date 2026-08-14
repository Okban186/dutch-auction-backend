package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.ConfirmProductMediaRequest;
import com.auction.dutch.model.dto.request.GenerateUploadUrlsRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.GenerateUploadUrlsResponse;
import com.auction.dutch.model.dto.response.ProductMediaResponse;
import com.auction.dutch.service.MediaUploadService;
import com.auction.dutch.service.ProductMediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MediaUploadController {

    private final MediaUploadService mediaUploadService;

    private final ProductMediaService productMediaService;

    @PostMapping("/admin/products/{id}/media/upload-urls")
    public GenerateUploadUrlsResponse generateUploadUrls(
            @PathVariable(name = "id") Long productId,
            @Valid @RequestBody GenerateUploadUrlsRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return mediaUploadService.generateUploadUrls(
                Long.valueOf(jwt.getSubject()),
                productId,
                request);
    }

    @PostMapping("/admin/products/{id}/media/confirm")
    public List<ProductMediaResponse> confirmUpload(
            @PathVariable(name = "id") Long productId,
            @Valid @RequestBody ConfirmProductMediaRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return productMediaService.confirmMedia(
                Long.valueOf(jwt.getSubject()),
                productId,
                request);
    }
}
