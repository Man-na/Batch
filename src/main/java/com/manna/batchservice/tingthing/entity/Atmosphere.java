package com.manna.batchservice.tingthing.entity;

import com.manna.batchservice.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atmospheres")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atmosphere extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int atmosphereId;

    private String atmosphereName;
}