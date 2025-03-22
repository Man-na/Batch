package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.InvitationLink;
import com.manna.batchservice.tingthing.entity.MatchingResult;
import com.manna.batchservice.tingthing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationLinkRepository extends JpaRepository<InvitationLink, Long> {
    List<InvitationLink> findByMatchingResult(MatchingResult matchingResult);

    List<InvitationLink> findByCreatedBy(User createdBy);

    Optional<InvitationLink> findByLinkCode(String linkCode);

    List<InvitationLink> findByIsActiveTrue();
}