package com.umc.commonplant.domain2.info.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {

    List<Info> findByName(String name);

    @Query("SELECT i FROM Info i WHERE (i.name = :keyword OR i.scientificName = :keyword) AND i.verified = true")
    Optional<Info> findVerifiedByNameOrScientificName(@Param("keyword") String keyword);

    @Query("SELECT i FROM Info i WHERE (i.name LIKE %:keyword% OR i.scientificName LIKE %:keyword%) AND i.verified = true")
    List<Info> findByNameOrScientificNameContainingAndVerified(@Param("keyword") String keyword);
}
