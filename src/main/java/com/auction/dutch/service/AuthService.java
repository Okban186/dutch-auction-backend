package com.auction.dutch.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auction.dutch.enums.UserStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.UserMapper;
import com.auction.dutch.model.dto.request.LoginRequest;
import com.auction.dutch.model.dto.response.ApiResponse;
import com.auction.dutch.model.dto.response.LoginResponse;
import com.auction.dutch.model.dto.response.UserResponse;
import com.auction.dutch.model.entity.Role;
import com.auction.dutch.model.entity.User;
import com.auction.dutch.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  @Value("${jwt.secret}")
  private String secret;

  private JWSSigner signer;
  private JWSVerifier verifier;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @PostConstruct
  public void init() {
    try {
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      this.signer = new MACSigner(keyBytes);
      this.verifier = new MACVerifier(keyBytes);
    } catch (JOSEException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public boolean interSpec() {
    return true;
  }

  public boolean verifier() {
    return true;
  }

  public String generateToken(User user) {
    Instant now = Instant.now();
    Instant expirationTime = now.plus(1, ChronoUnit.HOURS);
    List<String> cleanAuthorities = user.getRoles().stream()
        .map(Role::getName)
        .collect(Collectors.toList());
    try {
      JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
          .issuer("mmb.com")
          .subject(String.valueOf(user.getId()))
          .audience(Arrays.asList("https://mycompany.com", "frontend_app")) //
          .issueTime(Date.from(now))
          .expirationTime(Date.from(expirationTime))
          .jwtID(UUID.randomUUID().toString())
          .claim("roles", cleanAuthorities).build();
      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);
      signedJWT.sign(this.signer);
      return signedJWT.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException("Can't generate token", e.getCause());
    }
  }

  public LoginResponse userLogin(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    if (user.getStatus() == UserStatus.DELETED) {
      throw new AppException(ErrorCode.USER_DELETED);
    }

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
      throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    UserResponse userProfile = userMapper.toUserProfile(user);
    String accessToken = generateToken(user);

    LoginResponse loginResponse = LoginResponse.builder()
        .accessToken(accessToken)
        .userProfile(userProfile)
        .build();

    return loginResponse;
  }

}
