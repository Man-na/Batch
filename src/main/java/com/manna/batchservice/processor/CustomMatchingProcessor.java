package com.manna.batchservice.processor;

import com.manna.batchservice.tingthing.entity.*;
import com.manna.batchservice.tingthing.repository.CustomMatchingRepository;
import com.manna.batchservice.tingthing.repository.MatchingResultRepository;
import com.manna.batchservice.tingthing.repository.UserRepository;
import com.manna.batchservice.utils.MatchingProcessorUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomMatchingProcessor implements ItemProcessor<CustomMatching, MatchingResult> {

    @Autowired
    private CustomMatchingRepository customMatchingRepository;

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MatchingResult process(CustomMatching item) {
        // 현재 사용자 정보 가져오기
        User currentUser = item.getUser();
        int currentUserAge = MatchingProcessorUtils.calculateAge(currentUser.getBirth());
        String currentUserGender = currentUser.getGender().toString();

        // 동일한 location과 meeting_date를 가진 기존 매칭 조회
        List<MatchingResult> existingMatches = matchingResultRepository.findByLocationAndMeetingDate(
                item.getLocation(), item.getMeetingDate());

        // 나이 조건에 맞는 CustomMatching 후보 조회
        List<CustomMatching> candidates = customMatchingRepository.findByLocationAndMeetingDate(
                item.getLocation(), item.getMeetingDate());

        // 나이 조건에 맞는 후보 필터링
        List<CustomMatching> matchedCandidates = candidates.stream()
                .filter(candidate -> {
                    User candidateUser = candidate.getUser();
                    int candidateAge = MatchingProcessorUtils.calculateAge(candidateUser.getBirth());
                    return MatchingProcessorUtils.isAgeMatch(item.getAgePreference(), currentUserAge, candidateAge);
                })
                .toList();

        // 기존 매칭에 참여자를 추가할 수 있는지 확인
        Optional<MatchingResult> suitableMatch = existingMatches.stream()
                .filter(match -> MatchingProcessorUtils.canAddParticipant(match, currentUserGender))
                .findFirst();

        MatchingResult matchingResult;
        if (suitableMatch.isPresent()) {
            // 기존 매칭에 참여자 추가
            matchingResult = suitableMatch.get();
            MatchingParticipant newParticipant = MatchingParticipant.builder()
                    .matchingResult(matchingResult)
                    .user(currentUser)
                    .build();
            matchingResult.getParticipants().add(newParticipant);
        } else {
            // 새로운 매칭 생성
            matchingResult = MatchingResult.builder()
                    .matchingType(MatchingResult.MatchingType.CUSTOM)
                    .matchingId(item.getMatchingId())
                    .meetingDate(LocalDate.parse(item.getMeetingDate()))
                    .location(item.getLocation())
                    .status(MatchingResult.ResultStatus.SCHEDULED)
                    .build();

            // 참여자 추가 (필터링된 후보 기반)
            List<MatchingParticipant> participants = matchedCandidates.stream()
                    .filter(candidate -> MatchingProcessorUtils.canAddParticipant(matchingResult, candidate.getUser().getGender().toString()))
                    .map(candidate -> MatchingParticipant.builder()
                            .matchingResult(matchingResult)
                            .user(candidate.getUser())
                            .build())
                    .collect(Collectors.toList());
            matchingResult.setParticipants(participants);

            // 현재 사용자도 추가
            MatchingParticipant currentParticipant = MatchingParticipant.builder()
                    .matchingResult(matchingResult)
                    .user(currentUser)
                    .build();
            matchingResult.getParticipants().add(currentParticipant);
        }

        // 상태를 MATCHED로 변경
        item.setStatus(CustomMatching.MatchingStatus.MATCHED);

        return matchingResult;
    }


}