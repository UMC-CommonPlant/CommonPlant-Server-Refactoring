package com.umc.commonplant.domain.memo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m WHERE m.plant.plantIdx = :plantIdx")
    List<Memo> findByPlantIdx(@Param("plantIdx") Long plantIdx);
}
