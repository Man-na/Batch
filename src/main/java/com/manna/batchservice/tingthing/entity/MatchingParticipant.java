package com.manna.batchservice.tingthing.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "matching_participants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    private MatchingResult matchingResult;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus = AttendanceStatus.PENDING;

    public enum AttendanceStatus {
        PENDING, CONFIRMED, DECLINED
    }
}