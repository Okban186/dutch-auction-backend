package com.auction.dutch.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auction.dutch.enums.MediaType;
import com.auction.dutch.model.entity.ProductMedia;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {

    List<ProductMedia> findAllByProductId(Long productId);

    List<ProductMedia> findAllByIdInAndProductId(
            List<Long> ids,
            Long productId);

    @Modifying
    @Query("""
            DELETE FROM ProductMedia pm
            WHERE pm.product.id = :productId
              AND pm.id IN :mediaIds
            """)
    void deleteByProductIdAndMediaIds(
            Long productId,
            List<Long> mediaIds);

    long countByProductIdAndMediaType(
            Long productId,
            MediaType mediaType);

    List<ProductMedia> findAllByProductIdAndIdIn(
            Long productId,
            Collection<Long> ids);

    @Query("""
            SELECT pm.displayOrder
            FROM ProductMedia pm
            WHERE pm.product.id = :productId
              AND pm.displayOrder IN :orders
            """)
    List<Integer> findExistingDisplayOrders(
            @Param("productId") Long productId,
            @Param("orders") Collection<Integer> orders);

    Long countByProductId(Long productId);

    @Query(value = """
            SELECT DISTINCT ON (pm.product_id) pm.*
            FROM product_media pm
            WHERE pm.product_id IN (:productIds)
            ORDER BY pm.product_id, pm.display_order ASC
            """, nativeQuery = true)
    List<ProductMedia> findFirstMediaByProductIds(
            @Param("productIds") Collection<Long> productIds);
}
