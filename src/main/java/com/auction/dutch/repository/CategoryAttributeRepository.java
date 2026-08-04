package com.auction.dutch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.dutch.model.entity.CategoryAttribute;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Long> {

    @EntityGraph(attributePaths = "attribute")
    Optional<List<CategoryAttribute>> findByCategoryId(Long categoryId, Sort sort);

    List<CategoryAttribute> findAllByCategoryId(Long categoryId);

    @Modifying
    @Query("""
                DELETE FROM CategoryAttribute ca
                WHERE ca.category.id = :categoryId
                  AND ca.attribute.id IN :attributeIds
            """)
    void deleteByCategoryIdAndAttributeIds(
            Long categoryId,
            List<Long> attributeIds);
}
