package com.auction.dutch.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeResponse {

    private Long id;

    private String code;

    private String name;

    private List<CategoryTreeResponse> children;
}
