package com.umc.commonplant.domain.weather.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query(value = "select * from Weather w where w.nx=?1 and w.ny=?2 and w.fcstDate=?3 order by w.weather_idx desc LIMIT 1"
            ,nativeQuery = true)
    Weather findByGrid(String gridX, String gridY, String nowDate);
}
