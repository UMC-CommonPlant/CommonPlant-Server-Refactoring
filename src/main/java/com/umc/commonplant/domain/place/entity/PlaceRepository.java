package com.umc.commonplant.domain.place.entity;

import com.umc.commonplant.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p where p.code=?1")
    Optional<Place> getPlaceByCode(String code);
    List<Place> findAllByOwner(User user);
}
