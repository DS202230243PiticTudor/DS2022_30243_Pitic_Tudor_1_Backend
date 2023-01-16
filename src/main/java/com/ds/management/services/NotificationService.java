package com.ds.management.services;

import com.ds.management.domain.entities.Notification;
import com.ds.management.domain.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void addNotification(Notification notification) {
        this.notificationRepository.save(notification);
    }

    public void saveNotificationList(List<Notification> notificationList) {
        if(!notificationList.isEmpty()) {
            for(Notification notification : notificationList) {
                this.addNotification(notification);
            }
        }
    }

    public void deleteNotificationsByPersonId(UUID personId) {
        this.notificationRepository.deleteNotificationsByPersonId(personId);
    }

    public List<Notification> findAllByPersonIdAndDelete(UUID personId) {
        List<Notification> notificationList = this.notificationRepository.findNotificationsByPersonId(personId);
        if(notificationList.isEmpty()) {
            return notificationList;
        }
        this.notificationRepository.deleteAll(notificationList);
        return notificationList;
    }

    public String generateNotificationMessages(List<Notification> notificationList) {
        for (Notification notification : notificationList) {
            System.out.println(notification.toString());
        }
        return null;
    }
}
