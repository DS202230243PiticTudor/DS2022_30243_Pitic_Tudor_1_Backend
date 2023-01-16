package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findNotificationsByPersonId(UUID personId);
    void deleteNotificationsByPersonId(UUID personId);
}
