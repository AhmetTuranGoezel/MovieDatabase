package com.backend.repository;

import com.backend.model.WatchpartyInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchpartyInvitationRepository extends JpaRepository<WatchpartyInvitation,Integer> {
}
