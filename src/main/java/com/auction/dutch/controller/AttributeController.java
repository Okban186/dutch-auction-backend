package com.auction.dutch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.enums.AttributeStatus;
import com.auction.dutch.model.dto.request.CreateAttributeRequest;
import com.auction.dutch.model.dto.request.UpdatePartialAttributeRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.AttributeAdminResponse;
import com.auction.dutch.model.dto.response.AttributeResponse;
import com.auction.dutch.service.AttributeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping("/attributes")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAttributes() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", attributeService.getAttributes()));
    }

    @GetMapping("/attributes/{id}")
    public ResponseEntity<ApiResponse<AttributeResponse>> getAttribute(
            @PathVariable(name = "id") Long attributeId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", attributeService.getAttribute(attributeId)));
    }

    @GetMapping("/admin/attributes")
    public ResponseEntity<ApiResponse<List<AttributeAdminResponse>>> getAdminAttributes(
            @RequestParam(required = false) AttributeStatus status) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", attributeService.getAdminAttributes(status)));
    }

    @GetMapping("/admin/attributes/{id}")
    public ResponseEntity<ApiResponse<AttributeAdminResponse>> getAdminAttribute(
            @PathVariable(name = "id") Long attributeId) {
        return ResponseEntity
                .ok(new ApiResponse<>(200, "Successfull", attributeService.getAdminAttribute(attributeId)));
    }

    @PostMapping("/admin/attributes")
    public ResponseEntity<ApiResponse<AttributeAdminResponse>> createAttributes(
            @RequestBody CreateAttributeRequest attributeRequest) {
        return ResponseEntity
                .ok(new ApiResponse<>(200, "Successfull", attributeService.createAttribute(attributeRequest)));
    }

    @PatchMapping("/admin/attributes/{id}")
    public ResponseEntity<ApiResponse<AttributeAdminResponse>> updatePartialAttribute(
            @PathVariable(name = "id") Long attributeId, @RequestBody UpdatePartialAttributeRequest updateRequest) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfull",
                attributeService.updatePartialAttribute(attributeId, updateRequest)));
    }

    @DeleteMapping("/admin/attributes/{id}")
    public ResponseEntity deleteAttribute(@PathVariable(name = "id") Long attributeId) {
        attributeService.deleteAttribute(attributeId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/attributes/{id}/restore")
    public ResponseEntity restoreAttribute(@PathVariable(name = "id") Long attributeId) {
        attributeService.restoreAttribute(attributeId);
        return ResponseEntity.noContent().build();
    }

}
