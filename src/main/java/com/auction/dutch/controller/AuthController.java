package com.auction.dutch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.dutch.model.dto.request.LoginRequest;
import com.auction.dutch.model.dto.request.SignUpRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.LoginResponse;
import com.auction.dutch.model.dto.response.RegisterResponse;
import com.auction.dutch.service.AuthService;
import com.auction.dutch.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auths")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<ApiResponse<RegisterResponse>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(new ApiResponse<>(200, "Sign up successfull", userService.createUser(signUpRequest)));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(new ApiResponse<>(200, "Login successfull", authService.userLogin(loginRequest)));
  }

}
