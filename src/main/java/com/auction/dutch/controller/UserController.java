package com.auction.dutch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.UpdateProfileRequest;
import com.auction.dutch.model.dto.response.AdminUserResponse;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.MyProfileResponse;
import com.auction.dutch.model.dto.response.PublicUserProfileResponse;
import com.auction.dutch.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<MyProfileResponse>> getProfile(@AuthenticationPrincipal Jwt jwt) {

    MyProfileResponse myProfileResponse = userService.getMyProfile(Long.valueOf(jwt.getSubject()));

    return ResponseEntity.ok(new ApiResponse<MyProfileResponse>(200, "Successfull", myProfileResponse));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<PublicUserProfileResponse>> getPublicProfile(@PathVariable Long id) {
    PublicUserProfileResponse userProfile = userService.getPublicProfile(id);

    return ResponseEntity.ok(new ApiResponse<PublicUserProfileResponse>(200, "Successfull", userProfile));
  }

  @PreAuthorize("hasRole('Admin')")
  @GetMapping("/admin/{id}")
  public ResponseEntity<ApiResponse<AdminUserResponse>> getProfile(@PathVariable Long id) {
    AdminUserResponse userProfile = userService.getUserDetail(id);

    return ResponseEntity.ok(new ApiResponse<AdminUserResponse>(200, "Successfull", userProfile));
  }

  @PatchMapping("/me")
  public ResponseEntity<ApiResponse<MyProfileResponse>> updateUserProfile(@AuthenticationPrincipal Jwt jwt,
      @RequestBody UpdateProfileRequest request) {
    MyProfileResponse myProfileResponse = userService.updateProfileRequest(Long.valueOf(jwt.getSubject()), request);
    return ResponseEntity.ok(new ApiResponse<MyProfileResponse>(200, "Successfull", myProfileResponse));
  }

  @PreAuthorize("hasRole('Admin')")
  @DeleteMapping("/admin/{id}")
  public ResponseEntity<ApiResponse> solfDeleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
    userService.solfUserDelete(Long.valueOf(jwt.getSubject()), id);

    return ResponseEntity.ok(new ApiResponse<>(200, "Successfull", null));
  }

}
