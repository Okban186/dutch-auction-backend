package com.auction.dutch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.dutch.model.entity.AuctionSession;

@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, Long> {

}
