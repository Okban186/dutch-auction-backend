package com.auction.dutch.config.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.auction.dutch.config.properties.JwtProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

  private final JwtProperties jwtProperties;

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(
        jwtProperties.secret().getBytes(StandardCharsets.UTF_8),
        jwtProperties.macAlgorithm());

    NimbusJwtDecoder decoder = NimbusJwtDecoder
        .withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.from(jwtProperties.algorithm()))
        .build();
    return decoder;
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    SecretKeySpec secretKey = new SecretKeySpec(jwtProperties.secret().getBytes(), jwtProperties.macAlgorithm());
    return NimbusJwtEncoder.withSecretKey(secretKey).build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {

    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

    converter.setAuthoritiesClaimName("roles");
    converter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

    jwtConverter.setJwtGrantedAuthoritiesConverter(
        converter);

    return jwtConverter;
  }
}
