package com.auction.dutch.service;

import java.util.Collections;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auction.dutch.enums.UserStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.UserMapper;
import com.auction.dutch.model.dto.request.SignUpRequest;
import com.auction.dutch.model.dto.request.UpdateProfileRequest;
import com.auction.dutch.model.dto.response.AdminUserResponse;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.MyProfileResponse;
import com.auction.dutch.model.dto.response.PublicUserProfileResponse;
import com.auction.dutch.model.dto.response.RegisterResponse;
import com.auction.dutch.model.entity.Role;
import com.auction.dutch.model.entity.User;
import com.auction.dutch.repository.RoleRepository;
import com.auction.dutch.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final AuthService authService;

  private User getActiveUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    if (user.getStatus() == UserStatus.DELETED) {
      throw new AppException(ErrorCode.USER_DELETED);
    }

    return user;
  }

  public RegisterResponse createUser(SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername()))
      throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
    String passwordHash = passwordEncoder.encode(signUpRequest.getPassword());
    User user = userMapper.toUser(signUpRequest);
    user.setPasswordHash(passwordHash);

    Role userRole = roleRepository.findByCode("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    user.setRoles(Collections.singleton(userRole));
    log.info(signUpRequest.getDisplayName() + " " + user.getDisplayName());
    userRepository.save(user);
    RegisterResponse registerResponse = new RegisterResponse();
    registerResponse.setUserProfile(userMapper.toUserProfile(user));
    try {
      String accessToken = authService.generateToken(user);
      registerResponse.setAccessToken(accessToken);
    } catch (Exception e) {
      throw new RuntimeException("Can't generate access token", e.getCause());
    }

    return registerResponse;
  }

  public PublicUserProfileResponse getPublicProfile(Long id) {
    User user = getActiveUser(id);
    PublicUserProfileResponse userProfile = userMapper.toPublicProfile(user);
    return userProfile;
  }

  public MyProfileResponse getMyProfile(Long id) {
    User user = getActiveUser(id);
    MyProfileResponse userProfile = userMapper.toMyProfile(user);
    return userProfile;
  }

  public AdminUserResponse getUserDetail(Long id) {
    User user = getActiveUser(id);
    AdminUserResponse userDetail = userMapper.toAdminUser(user);
    return userDetail;
  }

  @Transactional
  public MyProfileResponse updateProfileRequest(Long id, UpdateProfileRequest request) {
    if (request.getDisplayName() == null && request.getPhoneNumber() == null && request.getUserAddresses() == null)
      return null;
    User user = getActiveUser(id);
    String displayName = user.getDisplayName();
    String phoneNumber = user.getPhoneNumber();
    String userAddress = user.getUserAddresses();
    userMapper.updateUserProfile(request, user);
    if (Objects.equals(displayName, user.getDisplayName()) && Objects.equals(phoneNumber, user.getPhoneNumber())
        && Objects.equals(userAddress, user.getUserAddresses()))
      return null;

    MyProfileResponse userProfile = userMapper.toMyProfile(user);

    return userProfile;
  }

  @Transactional
  public Boolean solfUserDelete(Long adminId, Long id) {
    if (adminId == id)
      throw new AppException(ErrorCode.CANNOT_DELETE_LAST_ADMIN);
    User user = getActiveUser(id);
    user.setStatus(UserStatus.DELETED);

    return true;

  }
}
