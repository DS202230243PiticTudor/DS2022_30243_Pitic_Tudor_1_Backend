package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
