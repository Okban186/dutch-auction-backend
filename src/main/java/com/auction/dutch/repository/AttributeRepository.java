package com.auction.dutch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.dutch.enums.AttributeStatus;
import com.auction.dutch.model.entity.AttributeDefinition;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeDefinition, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<AttributeDefinition> findAllByStatus(AttributeStatus status);
}
