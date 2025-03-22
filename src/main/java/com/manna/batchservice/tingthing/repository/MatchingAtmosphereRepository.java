package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.CustomMatching;
import com.manna.batchservice.tingthing.entity.MatchingAtmosphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchingAtmosphereRepository extends JpaRepository<MatchingAtmosphere, Long> {

    @Query("SELECT cm FROM MatchingAtmosphere atm JOIN atm.customMatching cm WHERE cm.status = 'PENDING' AND cm.meetingDate = ?1 AND cm.location = ?2 AND atm.atmosphere IN ?3 GROUP BY cm HAVING COUNT(DISTINCT atm.atmosphere) >= 1")
    List<CustomMatching> findCustomMatchingByAtmosphere(String meetingDate, String location, List<String> atmospheres);
}