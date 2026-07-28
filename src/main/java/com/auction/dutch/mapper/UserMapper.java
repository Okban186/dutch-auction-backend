package com.auction.dutch.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.auction.dutch.model.dto.request.SignUpRequest;
import com.auction.dutch.model.dto.request.UpdateProfileRequest;
import com.auction.dutch.model.dto.response.AdminUserResponse;
import com.auction.dutch.model.dto.response.MyProfileResponse;
import com.auction.dutch.model.dto.response.PublicUserProfileResponse;
import com.auction.dutch.model.dto.response.UserResponse;
import com.auction.dutch.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "passwordHash", ignore = true)
  User toUser(SignUpRequest signUpRequest);

  UserResponse toUserProfile(User user);

  PublicUserProfileResponse toPublicProfile(User user);

  AdminUserResponse toAdminUser(User user);

  MyProfileResponse toMyProfile(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateUserProfile(UpdateProfileRequest request, @MappingTarget User user);

  @Condition
  default Boolean shouldMap(String value) {
    return value != null && !value.isBlank();
  }
}
