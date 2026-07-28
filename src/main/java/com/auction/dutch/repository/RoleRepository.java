package com.auction.dutch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.dutch.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  public Optional<Role> findByCode(String code);
}
