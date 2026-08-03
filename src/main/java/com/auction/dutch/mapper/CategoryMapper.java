package com.auction.dutch.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.auction.dutch.model.dto.internal.CategoryBreadcrumbDto;
import com.auction.dutch.model.dto.request.CreateCategoryRequest;
import com.auction.dutch.model.dto.request.UpdateCategoryRequest;
import com.auction.dutch.model.dto.response.CategoryDetailResponse;
import com.auction.dutch.model.dto.response.CategorySummaryResponse;
import com.auction.dutch.model.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    List<CategorySummaryResponse> toCategorySummaryList(List<Category> categories);

    @Mapping(target = "cBreadcrumbDtos", ignore = true)
    CategoryDetailResponse toCategoryDetail(Category category);

    CategoryBreadcrumbDto toBreadcrumbDto(Category category);

    @Mapping(target = "parentId", ignore = true)
    Category toCategoryModel(CreateCategoryRequest categoryRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "parentId", target = "parentId", ignore = true)
    void updateCategory(UpdateCategoryRequest request, @MappingTarget Category category);
}
