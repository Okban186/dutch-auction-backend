package com.auction.dutch.model.dto.response;

import java.util.List;

import com.auction.dutch.model.dto.internal.CategoryBreadcrumbDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDetailResponse {

    private Long id;

    private String code;

    private String name;

    private List<CategoryBreadcrumbDto> cBreadcrumbDtos;
}
