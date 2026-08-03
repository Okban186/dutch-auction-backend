package com.auction.dutch.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.auction.dutch.enums.CategoryStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.CategoryMapper;
import com.auction.dutch.model.dto.internal.CategoryBreadcrumbDto;
import com.auction.dutch.model.dto.request.CreateCategoryRequest;
import com.auction.dutch.model.dto.request.UpdateCategoryRequest;
import com.auction.dutch.model.dto.response.CategoryAttributeItemResponse;
import com.auction.dutch.model.dto.response.CategoryDetailResponse;
import com.auction.dutch.model.dto.response.CategorySummaryResponse;
import com.auction.dutch.model.dto.response.CategoryTreeResponse;
import com.auction.dutch.model.entity.AttributeDefinition;
import com.auction.dutch.model.entity.Category;
import com.auction.dutch.model.entity.CategoryAttribute;
import com.auction.dutch.repository.CategoryAttributeRepository;
import com.auction.dutch.repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryAttributeRepository cAttributeRepository;

    private boolean isDescendant(Category category, Category candidateParent) {
        Category current = candidateParent;

        while (current != null) {

            if (current.getId().equals(category.getId())) {
                return true;
            }

            current = current.getParent();
        }

        return false;
    }

    public Category getCategoryActive(Long id, String customeMessage) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, customeMessage));
        if (category.getStatus() == CategoryStatus.DELETED)
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        return category;

    }

    public List<CategorySummaryResponse> getCategorySummary() {
        List<Category> categories = categoryRepository.findAll();
        List<CategorySummaryResponse> cSummaryResponses = categoryMapper.toCategorySummaryList(categories);

        return cSummaryResponses;
    }

    public CategoryDetailResponse getCategoryDetail(Long id) {
        Category category = getCategoryActive(id, "");
        return getCategoryDetail(category);
    }

    public CategoryDetailResponse getCategoryDetail(Category category) {

        CategoryDetailResponse baseResponse = categoryMapper.toCategoryDetail(category);

        List<CategoryBreadcrumbDto> cBreadcrumbDtos = new ArrayList<>();
        cBreadcrumbDtos.add(categoryMapper.toBreadcrumbDto(category));

        Category current = category;
        while (current.getParent() != null) {
            current = current.getParent();
            cBreadcrumbDtos.add(categoryMapper.toBreadcrumbDto(current));
        }

        return new CategoryDetailResponse(
                baseResponse.id(),
                baseResponse.code(),
                baseResponse.name(),
                cBreadcrumbDtos);
    }

    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, CategoryTreeResponse> map = new HashMap<>();

        for (Category category : categories) {
            if (category.getStatus() == CategoryStatus.DELETED)
                continue;
            CategoryTreeResponse node = CategoryTreeResponse.builder()
                    .id(category.getId())
                    .code(category.getCode())
                    .name(category.getName())
                    .children(new ArrayList<>()).build();

            map.put(category.getId(), node);
        }

        List<CategoryTreeResponse> roots = new ArrayList<>();

        for (Category category : categories) {

            CategoryTreeResponse node = map.get(category.getId());

            if (category.getParentId() == null) {

                roots.add(node);

            } else {

                map.get(category.getParentId())
                        .children()
                        .add(node);
            }
        }

        return roots;
    }

    public void createCategory(CreateCategoryRequest categoryRequest) {
        Category newCategory = categoryMapper.toCategoryModel(categoryRequest);

        if (categoryRequest.parentId() != null) {

            Category parent = getCategoryActive(categoryRequest.parentId(), "Parent category is not found or inactive");
            newCategory.setParent(parent);
        }

        categoryRepository.save(newCategory);
    }

    @Transactional
    public CategoryDetailResponse updateCategory(Long id, UpdateCategoryRequest categoryRequest) {
        Category category = getCategoryActive(id, null);
        categoryMapper.updateCategory(categoryRequest, category);
        if (categoryRequest.parentId() != null) {
            if (categoryRequest.parentId().equals(id)) {
                throw new AppException(ErrorCode.CATEGORY_CANNOT_BE_ITS_OWN_PARENT);
            }
            Category parent = getCategoryActive(categoryRequest.parentId(), "Parent is not found");
            if (isDescendant(category, parent))
                throw new AppException(ErrorCode.INVALID_CATEGORY_PARENT);
            category.setParent(parent);
        }
        return getCategoryDetail(category);
    }

    @Transactional
    public void solfDelete(Long id) {
        Category category = getCategoryActive(id, null);
        category.setStatus(CategoryStatus.DELETED);
    }

}