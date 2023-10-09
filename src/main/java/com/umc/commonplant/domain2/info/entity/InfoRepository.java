package com.umc.commonplant.domain2.info.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {

    List<Info> findByName(String name);

    List<Info> findByScientificName(String scientificName);

    Optional<Info> findByNameOrScientificName(String name, String scientificName);
}
