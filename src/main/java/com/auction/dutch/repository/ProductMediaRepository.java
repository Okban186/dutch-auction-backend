package com.auction.dutch.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
