package com.manna.batchservice.utils;

import com.manna.batchservice.tingthing.entity.CustomMatching;
import com.manna.batchservice.tingthing.entity.MatchingResult;

import java.time.LocalDate;
import java.time.Period;

public class MatchingProcessorUtils {

    private static final int MAX_MALE_PER_MATCH = 4;
    private static final int MAX_FEMALE_PER_MATCH = 4;

    // 나이 계산 메서드
    public static int calculateAge(String birth) {
        LocalDate birthDate = LocalDate.parse(birth);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // 나이 조건 체크 메서드
    public static boolean isAgeMatch(CustomMatching.AgePreference preference, int currentAge, int candidateAge) {
        switch (preference) {
            case PEER:
                return Math.abs(currentAge - candidateAge) <= 3;
            case HIGHER:
                return Math.abs(currentAge - candidateAge) <= 5;
            case NONE:
            default:
                return true;
        }
    }

    // 성별 제한 체크 메서드
    public static boolean canAddParticipant(MatchingResult match, String gender) {
        long maleCount = match.getParticipants().stream()
                .filter(p -> "MALE".equals(p.getUser().getGender().toString()))
                .count();
        long femaleCount = match.getParticipants().stream()
                .filter(p -> "FEMALE".equals(p.getUser().getGender().toString()))
                .count();

        if ("MALE".equals(gender)) {
            return maleCount < MAX_MALE_PER_MATCH;
        } else if ("FEMALE".equals(gender)) {
            return femaleCount < MAX_FEMALE_PER_MATCH;
        }
        return false;
    }
}
