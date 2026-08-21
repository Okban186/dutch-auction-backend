package com.auction.dutch.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.auction.dutch.model.dto.response.ProductDetailResponse;
import com.auction.dutch.model.dto.response.ProductItemResponse;
import com.auction.dutch.model.dto.response.ProductMediaResponse;
import com.auction.dutch.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductItemResponse> toItemResponses(List<Product> product);

    @Mapping(target = "productMediaResponses", source = "media")
    ProductDetailResponse toDetailResponse(Product product, List<ProductMediaResponse> media);
}
