package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.MatchingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchingResultRepository extends JpaRepository<MatchingResult, Long> {
    List<MatchingResult> findByMatchingTypeAndMatchingId(MatchingResult.MatchingType matchingType, Long matchingId);

    @Query("SELECT mr FROM MatchingResult mr JOIN mr.participants p WHERE p.user.id = ?1")
    List<MatchingResult> findByUserId(Long userId);

    List<MatchingResult> findByStatus(MatchingResult.ResultStatus status);

    List<MatchingResult> findByLocationAndMeetingDate(String location, String meetingDate);

    List<MatchingResult> findByMeetingDate(LocalDate meetingDate);}