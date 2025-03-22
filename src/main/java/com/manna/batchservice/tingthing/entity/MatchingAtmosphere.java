package com.manna.batchservice.tingthing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching_atmosphere")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingAtmosphere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchingAtmosphereId;

    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private CustomMatching customMatching;

    @ManyToOne
    @JoinColumn(name = "atmosphere_id", nullable = false)
    private Atmosphere atmosphere;
}