package com.auction.dutch.model.dto.response;

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
public class CategoryAttributeItemResponse {
    private Long id;

    private String code;

    private String name;

    private boolean required;

    private Integer displayOrder;
}
