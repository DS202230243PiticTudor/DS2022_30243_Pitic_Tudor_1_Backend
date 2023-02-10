package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.IndividualChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndividualChatRepository extends JpaRepository<IndividualChat, UUID> {
}
