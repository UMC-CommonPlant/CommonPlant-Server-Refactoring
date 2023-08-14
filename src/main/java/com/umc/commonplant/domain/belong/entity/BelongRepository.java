package com.umc.commonplant.domain.belong.entity;

import com.umc.commonplant.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BelongRepository extends JpaRepository<Belong, Long> {
    @Query("select b.user from Belong b where b.place.code=?1")
    Optional<List<User>> getUserByPlaceCode(String code);
}
