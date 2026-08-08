package com.auction.dutch.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.auction.dutch.model.dto.request.CreateAttributeRequest;
import com.auction.dutch.model.dto.request.UpdatePartialAttributeRequest;
import com.auction.dutch.model.dto.response.AttributeAdminResponse;
import com.auction.dutch.model.dto.response.AttributeResponse;
import com.auction.dutch.model.entity.AttributeDefinition;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    AttributeResponse toResponse(AttributeDefinition aDefinition);

    List<AttributeResponse> toResponses(List<AttributeDefinition> aDefinitions);

    AttributeAdminResponse toAdminResponse(AttributeDefinition aDefinition);

    List<AttributeAdminResponse> toAdminResponses(List<AttributeDefinition> aDefinitions);

    AttributeDefinition toEntity(CreateAttributeRequest attributeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartialAttribute(UpdatePartialAttributeRequest request, @MappingTarget AttributeDefinition aDefinition);
}
