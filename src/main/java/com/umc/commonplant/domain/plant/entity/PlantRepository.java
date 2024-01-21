package com.umc.commonplant.domain.plant.entity;

import com.umc.commonplant.domain.place.entity.Place;
import com.umc.commonplant.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    @Query("select p from Plant p where p.plantIdx=?1")
    Optional<Plant> findByPlantIdx(Long plantIdx);

    @Query(value = "SELECT p FROM Plant p WHERE p.place= ?1 ")
    List<Plant> findAllByPlace(Place place);

    String countPlantsByPlace(Place place);
}
