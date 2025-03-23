package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.RapidMatching;
import com.manna.batchservice.tingthing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapidMatchingRepository extends JpaRepository<RapidMatching, Long> {
    List<RapidMatching> findByUser(User user);

    List<RapidMatching> findByStatus(RapidMatching.MatchingStatus status);

    @Query("SELECT rm FROM RapidMatching rm WHERE rm.status = 'PENDING' AND " +
            "(rm.priority1Day = ?1 OR rm.priority1Day = ?2 OR " +
            "rm.priority2Day = ?1 OR rm.priority2Day = ?2)")
    List<RapidMatching> findPotentialMatches(String day1, String day2);

    @Query("SELECT rm FROM RapidMatching rm WHERE rm.status = 'PENDING' AND " +
            "rm.agePreference = ?1 AND " +
            "(rm.priority1Day = ?2 OR rm.priority1Day = ?3 OR " +
            "rm.priority2Day = ?2 OR rm.priority2Day = ?3)")
    List<RapidMatching> findPotentialMatchesByAgePreference(String agePreference, String day1, String day2);
}