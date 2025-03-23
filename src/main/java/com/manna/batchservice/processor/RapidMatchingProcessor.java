package com.manna.batchservice.processor;

import com.manna.batchservice.tingthing.entity.*;
import com.manna.batchservice.tingthing.repository.CustomMatchingRepository;
import com.manna.batchservice.tingthing.repository.MatchingResultRepository;
import com.manna.batchservice.tingthing.repository.RapidMatchingRepository;
import com.manna.batchservice.tingthing.repository.UserRepository;
import com.manna.batchservice.utils.MatchingProcessorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RapidMatchingProcessor implements ItemProcessor<RapidMatching, MatchingResult> {

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    @Autowired
    private CustomMatchingRepository customMatchingRepository;

    private static final List<String> VALID_DAYS = List.of("월", "화", "수", "목", "금", "토", "일");

    @Override
    public MatchingResult process(RapidMatching item) {
        // 우선순위 날짜 유효성 검사
        if (!VALID_DAYS.contains(item.getPriority1Day()) || (item.getPriority2Day() != null && !VALID_DAYS.contains(item.getPriority2Day()))) {
            throw new IllegalArgumentException("priority1Day 또는 priority2Day는 월, 화, 수, 목, 금, 토, 일 중 하나여야 합니다.");
        }

        // 현재 사용자 정보 가져오기
        User currentUser = item.getUser();
        int currentUserAge = MatchingProcessorUtils.calculateAge(currentUser.getBirth());
        String currentUserGender = currentUser.getGender().toString();

        // 우선순위 날짜 기반으로 가까운 4개의 잠재 날짜 계산
        List<LocalDate> potentialDates = calculatePotentialDates(item.getPriority1Day(), item.getPriority2Day());

        // 기존 매칭에 참여 시도
        MatchingResult matchingResult = null;
        for (LocalDate date : potentialDates) {
            List<MatchingResult> existingMatches = matchingResultRepository.findByMeetingDate(date);
            Optional<MatchingResult> suitableMatch = existingMatches.stream()
                    .filter(match -> MatchingProcessorUtils.canAddParticipant(match, currentUserGender))
                    .findFirst();

            if (suitableMatch.isPresent()) {
                matchingResult = suitableMatch.get();
                MatchingParticipant newParticipant = MatchingParticipant.builder()
                        .matchingResult(matchingResult)
                        .user(currentUser)
                        .build();
                matchingResult.getParticipants().add(newParticipant);
                break;
            }
        }

        // 적합한 기존 매칭이 없으면 CustomMatching에서 후보 조회
        if (matchingResult == null) {
            LocalDate meetingDate = potentialDates.get(0);

            List<CustomMatching> candidates = customMatchingRepository.findByStatus(CustomMatching.MatchingStatus.PENDING)
                    .stream()
                    .filter(cm -> {
                        User candidateUser = cm.getUser();
                        int candidateAge = MatchingProcessorUtils.calculateAge(candidateUser.getBirth());
                        return MatchingProcessorUtils.isAgeMatch(item.getAgePreference(), currentUserAge, candidateAge);
                    })
                    .filter(cm -> {
                        LocalDate candidateDate = LocalDate.parse(cm.getMeetingDate());
                        return potentialDates.contains(candidateDate);
                    })
                    .collect(Collectors.toList());

            if (!candidates.isEmpty()) {
                String location = candidates.get(0).getLocation();

                // matchingResult 생성
                matchingResult = MatchingResult.builder()
                        .matchingType(MatchingResult.MatchingType.RAPID)
                        .matchingId(item.getMatchingId())
                        .meetingDate(meetingDate)
                        .location(location)
                        .status(MatchingResult.ResultStatus.SCHEDULED)
                        .build();

                // matchingResult를 별도의 변수로 고정
                final MatchingResult finalMatchingResult = matchingResult;

                // CustomMatching 후보를 참여자로 추가하며 상태를 MATCHED로 변경
                List<MatchingParticipant> participants = candidates.stream()
                        .filter(candidate -> MatchingProcessorUtils.canAddParticipant(finalMatchingResult, candidate.getUser().getGender().toString()))
                        .map(candidate -> {
                            candidate.setStatus(CustomMatching.MatchingStatus.MATCHED);
                            return MatchingParticipant.builder()
                                    .matchingResult(finalMatchingResult)
                                    .user(candidate.getUser())
                                    .build();
                        })
                        .collect(Collectors.toList());
                finalMatchingResult.setParticipants(participants);

                // 현재 사용자를 참여자로 추가
                MatchingParticipant currentParticipant = MatchingParticipant.builder()
                        .matchingResult(finalMatchingResult)
                        .user(currentUser)
                        .build();
                finalMatchingResult.getParticipants().add(currentParticipant);
            } else {
                return null;
            }
        }

        // RapidMatching 상태를 MATCHED로 업데이트
        item.setStatus(RapidMatching.MatchingStatus.MATCHED);

        return matchingResult;
    }

    // 우선순위 날짜 기반으로 가까운 4개 날짜 계산
    private List<LocalDate> calculatePotentialDates(String priority1Day, String priority2Day) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.plusDays(i);
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            if (dayOfWeek.equals(priority1Day) || (dayOfWeek.equals(priority2Day))) {
                dates.add(date);
                if (dates.size() == 4) break;
            }
        }
        return dates;
    }
}