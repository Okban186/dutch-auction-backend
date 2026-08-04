package com.auction.dutch.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.auction.dutch.enums.CategoryStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.model.dto.request.CategoryAttributeUpdateRequest;
import com.auction.dutch.model.dto.request.UpdateCategoryAttributesRequest;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.model.entity.AttributeDefinition;
import com.auction.dutch.model.entity.Category;
import com.auction.dutch.model.entity.CategoryAttribute;
import com.auction.dutch.repository.AttributeRepository;
import com.auction.dutch.repository.CategoryAttributeRepository;
import com.auction.dutch.repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryAttributeService {

    private final CategoryAttributeRepository cAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    public Category getCategoryActive(Long id, String customeMessage) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, customeMessage));
        if (category.getStatus() == CategoryStatus.DELETED)
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        return category;

    }

    public List<CategoryAttributeItemResponse> getOwnAttributes(Long id) {

        List<CategoryAttribute> categoryAttributes = cAttributeRepository
                .findByCategoryId(id, Sort.by("displayOrder").ascending())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        List<CategoryAttributeItemResponse> attributes = new LinkedList<>();
        for (CategoryAttribute cAttribute : categoryAttributes) {
            AttributeDefinition aDefinition = cAttribute.getAttribute();
            CategoryAttributeItemResponse cAttributeResponse = CategoryAttributeItemResponse.builder()
                    .id(aDefinition.getId())
                    .code(aDefinition.getCode())
                    .name(aDefinition.getName())
                    .required(cAttribute.getRequired())
                    .displayOrder(cAttribute.getDisplayOrder())
                    .build();

            attributes.add(cAttributeResponse);
        }

        return attributes;
    }

    public List<CategoryAttributeItemResponse> getResolvedAttributes(Long cateoryId) {

        Category category = getCategoryActive(cateoryId, "");
        List<Long> hierarchy = new ArrayList<>();

        while (category != null) {
            hierarchy.add(category.getId());
            category = category.getParent();
        }

        Collections.reverse(hierarchy);
        Set<CategoryAttributeItemResponse> result = new LinkedHashSet<>();
        for (Long id : hierarchy) {
            List<CategoryAttribute> categoryAttributes = cAttributeRepository
                    .findByCategoryId(id, Sort.by("displayOrder").ascending())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            for (CategoryAttribute cAttribute : categoryAttributes) {
                AttributeDefinition aDefinition = cAttribute.getAttribute();
                CategoryAttributeItemResponse cAttributeResponse = CategoryAttributeItemResponse.builder()
                        .id(aDefinition.getId())
                        .code(aDefinition.getCode())
                        .name(aDefinition.getName())
                        .required(cAttribute.getRequired())
                        .displayOrder(cAttribute.getDisplayOrder())
                        .build();

                result.add(cAttributeResponse);
            }
        }

        return new ArrayList<>(result);
    }

    @Transactional
    public List<CategoryAttributeItemResponse> updateCategoryAttributes(Long categoryId,
            UpdateCategoryAttributesRequest request) {
        Category category = getCategoryActive(categoryId, null);

        List<CategoryAttribute> currentAttributes = cAttributeRepository.findAllByCategoryId(categoryId);

        Map<Long, CategoryAttribute> oldAttributes = currentAttributes.stream()
                .collect(Collectors.toMap(
                        CategoryAttribute::getAttributeId,
                        Function.identity()));

        Set<Long> attributeIds = request.attributes()
                .stream()
                .map(CategoryAttributeUpdateRequest::attributeId)
                .collect(Collectors.toSet());

        Map<Long, AttributeDefinition> attributeMap = attributeRepository.findAllById(attributeIds)
                .stream()
                .collect(Collectors.toMap(
                        AttributeDefinition::getId,
                        Function.identity()));

        List<CategoryAttribute> toInsert = new ArrayList<>();

        for (CategoryAttributeUpdateRequest item : request.attributes()) {

            Long attributeId = item.attributeId();

            CategoryAttribute existing = oldAttributes.remove(attributeId);

            if (existing != null) {

                if (!Objects.equals(
                        existing.getRequired(),
                        item.required())) {

                    existing.setRequired(item.required());
                }

                if (!Objects.equals(
                        existing.getDisplayOrder(),
                        item.displayOrder())) {

                    existing.setDisplayOrder(item.displayOrder());
                }

            } else {

                AttributeDefinition attribute = attributeMap.get(attributeId);

                if (attribute == null) {
                    throw new AppException(
                            ErrorCode.ATTRIBUTE_NOT_FOUND);
                }

                CategoryAttribute newCategoryAttribute = CategoryAttribute.builder()
                        .category(category)
                        .attribute(attribute)
                        .required(item.required())
                        .displayOrder(item.displayOrder())
                        .build();

                toInsert.add(newCategoryAttribute);
            }
        }

        List<Long> removedAttributeIds = oldAttributes.values()
                .stream()
                .map(CategoryAttribute::getAttributeId)
                .toList();

        if (!removedAttributeIds.isEmpty()) {
            cAttributeRepository
                    .deleteByCategoryIdAndAttributeIds(
                            categoryId,
                            removedAttributeIds);
        }

        if (!toInsert.isEmpty()) {
            cAttributeRepository.saveAll(toInsert);
        }

        return getOwnAttributes(categoryId);
    }
}
