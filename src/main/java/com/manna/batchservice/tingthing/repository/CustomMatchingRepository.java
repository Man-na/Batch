package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.CustomMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomMatchingRepository extends JpaRepository<CustomMatching, Long> {
    @Query("SELECT cm FROM CustomMatching cm JOIN cm.atmospheres atm WHERE cm.status = 'PENDING' AND cm.meetingDate = ?1 AND cm.location = ?2 AND atm.atmosphere IN ?3 GROUP BY cm HAVING COUNT(DISTINCT atm.atmosphere) >= 1")
    List<CustomMatching> findCustomMatchingByAtmosphere(String meetingDate, String location, List<String> atmospheres);

    List<CustomMatching> findByLocationAndMeetingDate(String location, String meetingDate);

    List<CustomMatching> findByStatus(CustomMatching.MatchingStatus status);
}