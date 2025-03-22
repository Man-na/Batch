package com.manna.batchservice.tingthing.entity;

import com.manna.batchservice.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rapid_matching")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapidMatching extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String priority1Day;

    private String priority2Day;

    @Column(name = "age_preference")
    @Enumerated(EnumType.STRING)
    private AgePreference agePreference = AgePreference.NONE;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status = MatchingStatus.PENDING;

    public enum MatchingStatus {
        PENDING, MATCHED, CANCELED
    }

    public enum AgePreference {
        NONE, PEER, HIGHER
    }
}