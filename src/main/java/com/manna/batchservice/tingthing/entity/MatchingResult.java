package com.manna.batchservice.tingthing.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matching_result")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resultId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingType matchingType;

    private int matchingId;

    private LocalDate meetingDate;

    private String location;

    @Enumerated(EnumType.STRING)
    private ResultStatus status = ResultStatus.SCHEDULED;

    @OneToMany(mappedBy = "matchingResult", cascade = CascadeType.ALL)
    private List<MatchingParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "matchingResult", cascade = CascadeType.ALL)
    private List<InvitationLink> invitationLinks = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum MatchingType {
        RAPID, CUSTOM
    }

    public enum ResultStatus {
        SCHEDULED, COMPLETED, CANCELED
    }
}