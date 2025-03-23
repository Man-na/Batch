package com.manna.batchservice.tingthing.entity;

import com.manna.batchservice.common.constants.Enums;
import com.manna.batchservice.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "custom_matching")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMatching extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id", nullable = false)
    private int matchingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "meeting_date", nullable = false)
    private String meetingDate;

    @Column(nullable = false)
    private String location;

    @Column(name = "age_preference")
    @Enumerated(EnumType.STRING)
    private Enums.AgePreference agePreference = Enums.AgePreference.NONE;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status = MatchingStatus.PENDING;

    @OneToMany(mappedBy = "customMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchingAtmosphere> atmospheres = new ArrayList<>();

    public enum MatchingStatus {
        PENDING, MATCHED, CANCELED
    }
}