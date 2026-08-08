package com.auction.dutch.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.auction.dutch.enums.AttributeDataType;
import com.auction.dutch.enums.AttributeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_definitions")
public class AttributeDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false, unique = true)
  private String name;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private AttributeStatus status = AttributeStatus.ACTIVE;

  @Column(name = "data_type")
  @Enumerated(EnumType.STRING)
  private AttributeDataType dataType;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
