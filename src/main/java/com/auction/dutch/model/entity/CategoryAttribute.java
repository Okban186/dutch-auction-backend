package com.auction.dutch.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "category_attributes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "category_id",
        "attribute_id"
    })
})
public class CategoryAttribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Category category;

  @ManyToOne
  private AttributeDefinition attribute;

  @Column(name = "is_required", nullable = false)
  private Boolean required;

  @Column(name = "display_order", nullable = false)
  private int displayOrder;

  private LocalDateTime createdAt;
}
