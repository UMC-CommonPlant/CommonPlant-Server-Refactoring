package com.umc.commonplant.domain2.history.entity;

import com.umc.commonplant.domain2.info.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByInfo(Info info);
}
