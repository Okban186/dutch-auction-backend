package com.auction.dutch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auction.dutch.enums.AttributeStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.AttributeMapper;
import com.auction.dutch.model.dto.request.CreateAttributeRequest;
import com.auction.dutch.model.dto.request.UpdatePartialAttributeRequest;
import com.auction.dutch.model.dto.response.AttributeAdminResponse;
import com.auction.dutch.model.dto.response.AttributeResponse;
import com.auction.dutch.model.entity.AttributeDefinition;
import com.auction.dutch.repository.AttributeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;

    private final AttributeMapper attributeMapper;

    public AttributeDefinition getActiveAttribute(Long attributeId) {
        AttributeDefinition attributeDefinition = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        if (attributeDefinition.getStatus() == AttributeStatus.DELETED)
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);

        return attributeDefinition;
    }

    public List<AttributeResponse> getAttributes() {

        return attributeMapper.toResponses(attributeRepository.findAllByStatus(AttributeStatus.ACTIVE));
    }

    public AttributeResponse getAttribute(Long attributeId) {
        AttributeDefinition attributeDefinition = getActiveAttribute(attributeId);
        AttributeResponse attributeResponse = attributeMapper.toResponse(attributeDefinition);

        return attributeResponse;
    }

    public List<AttributeAdminResponse> getAdminAttributes(AttributeStatus status) {

        return attributeMapper.toAdminResponses(
                status == null
                        ? attributeRepository.findAll()
                        : attributeRepository.findAllByStatus(status));
    }

    public AttributeAdminResponse getAdminAttribute(Long attributeId) {
        AttributeDefinition attributeDefinition = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        AttributeAdminResponse attributeResponse = attributeMapper.toAdminResponse(attributeDefinition);

        return attributeResponse;
    }

    public AttributeAdminResponse createAttribute(CreateAttributeRequest attributeRequest) {
        boolean codeExists = attributeRepository.existsByCode(attributeRequest.code());
        boolean nameExists = attributeRepository.existsByName(attributeRequest.name());

        if (codeExists)
            throw new AppException(ErrorCode.ATTRIBUTE_CODE_ALREADY_EXISTS);
        if (nameExists)
            throw new AppException(ErrorCode.ATTRIBUTE_NAME_ALREADY_EXISTS);

        AttributeDefinition attributeDefinition = attributeMapper.toEntity(attributeRequest);
        attributeRepository.save(attributeDefinition);

        return attributeMapper.toAdminResponse(attributeDefinition);
    }

    @Transactional
    public AttributeAdminResponse updatePartialAttribute(Long attributeId,
            UpdatePartialAttributeRequest updateRequest) {
        boolean codeExists = attributeRepository.existsByCode(updateRequest.code());
        boolean nameExists = attributeRepository.existsByName(updateRequest.name());

        if (codeExists)
            throw new AppException(ErrorCode.ATTRIBUTE_CODE_ALREADY_EXISTS);
        if (nameExists)
            throw new AppException(ErrorCode.ATTRIBUTE_NAME_ALREADY_EXISTS);

        AttributeDefinition aDefinition = getActiveAttribute(attributeId);
        attributeMapper.updatePartialAttribute(updateRequest, aDefinition);

        return attributeMapper.toAdminResponse(aDefinition);
    }

    @Transactional
    public void deleteAttribute(Long attributeId) {
        AttributeDefinition aDefinition = getActiveAttribute(attributeId);
        aDefinition.setStatus(AttributeStatus.DELETED);
    }

    @Transactional
    public void restoreAttribute(Long attributeId) {
        AttributeDefinition aDefinition = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        if (aDefinition.getStatus() == AttributeStatus.ACTIVE)
            return;
        aDefinition.setStatus(AttributeStatus.ACTIVE);
    }
}
