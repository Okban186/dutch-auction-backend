package com.auction.dutch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.dutch.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
