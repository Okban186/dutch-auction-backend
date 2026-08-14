package com.auction.dutch.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.auction.dutch.model.dto.response.ProductDetailResponse;
import com.auction.dutch.model.dto.response.ProductItemResponse;
import com.auction.dutch.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductItemResponse> toItemResponses(List<Product> product);

    ProductDetailResponse toDetailResponse(Product product);
}
