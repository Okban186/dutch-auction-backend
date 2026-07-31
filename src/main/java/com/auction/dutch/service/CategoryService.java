package com.auction.dutch.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.CategoryMapper;
import com.auction.dutch.model.dto.internal.CategoryBreadcrumbDto;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.model.dto.response.CategoryDetailResponse;
import com.auction.dutch.model.dto.response.CategorySummaryResponse;
import com.auction.dutch.model.dto.response.CategoryTreeResponse;
import com.auction.dutch.model.entity.AttributeDefinition;
import com.auction.dutch.model.entity.Category;
import com.auction.dutch.model.entity.CategoryAttribute;
import com.auction.dutch.repository.CategoryAttributeRepository;
import com.auction.dutch.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryAttributeRepository cAttributeRepository;

    public List<CategorySummaryResponse> getCategorySummary() {
        List<Category> categories = categoryRepository.findAll();
        List<CategorySummaryResponse> cSummaryResponses = categoryMapper.toCategorySummaryList(categories);

        return cSummaryResponses;
    }

    public CategoryDetailResponse getCategoryDetail(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryDetailResponse cDetailResponse = categoryMapper.toCategoryDetail(category);

        List<CategoryBreadcrumbDto> cBreadcrumbDtos = new ArrayList<>();

        cBreadcrumbDtos.add(categoryMapper.toBreadcrumbDto(category));
        Category current = category;
        while (current.getParent() != null) {
            current = current.getParent();
            cBreadcrumbDtos.add(categoryMapper.toBreadcrumbDto(current));
        }

        cDetailResponse.setCBreadcrumbDtos(cBreadcrumbDtos);

        return cDetailResponse;
    }

    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, CategoryTreeResponse> map = new HashMap<>();

        for (Category category : categories) {
            CategoryTreeResponse node = new CategoryTreeResponse();
            node.setId(category.getId());
            node.setCode(category.getCode());
            node.setName(category.getName());
            node.setChildren(new ArrayList<>());

            map.put(category.getId(), node);
        }

        List<CategoryTreeResponse> roots = new ArrayList<>();

        for (Category category : categories) {

            CategoryTreeResponse node = map.get(category.getId());

            if (category.getParentId() == null) {

                roots.add(node);

            } else {

                map.get(category.getParentId())
                        .getChildren()
                        .add(node);
            }
        }

        return roots;
    }

    public List<CategoryAttributeItemResponse> getCategoryAttributeItem(Long cateoryId) {

        Category category = categoryRepository.findById(cateoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        List<Long> hierarchy = new ArrayList<>();

        while (category != null) {
            hierarchy.add(category.getId());
            category = category.getParent();
        }

        Collections.reverse(hierarchy);
        List<CategoryAttributeItemResponse> result = new ArrayList<>();
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

        return result;
    }

}
