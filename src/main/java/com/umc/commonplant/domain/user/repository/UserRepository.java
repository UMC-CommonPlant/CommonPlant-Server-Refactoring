package com.umc.commonplant.domain.user.repository;

import com.umc.commonplant.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByname(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(String uuid);

    @Query("select u from User u where u.email=?1 and u.provider=?2")
    User findByEmail(String email, String loginType);
    @Query("select count(u) from User u where u.email=?1 and u.provider=?2")
    int countUserByEmail(String email, String provider);

    @Query("select u from User u where u.email=?1 and u.provider=?2")
    User findUserByEmail(String email, String loginType);
}
