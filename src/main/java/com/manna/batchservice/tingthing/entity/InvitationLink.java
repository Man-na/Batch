package com.manna.batchservice.tingthing.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    private MatchingResult matchingResult;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, unique = true)
    private String linkCode;

    private LocalDateTime expiresAt;

    private boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}