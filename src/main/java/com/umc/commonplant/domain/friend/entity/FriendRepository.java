package com.umc.commonplant.domain.friend.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findBySenderAndReceiverAndStatus(String sender, String receiver, String status);
}
