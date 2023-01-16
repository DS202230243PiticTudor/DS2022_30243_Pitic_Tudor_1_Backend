package com.ds.management.services;

import com.ds.management.domain.dtos.ResponseMessage;
import com.ds.management.domain.entities.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate, NotificationService notificationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessage responseMessage = new ResponseMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", responseMessage);
    }

    public void notifyFrontendUserAndDeleteNotifications(final String personIdString) {
        List<Notification> notificationList = this.notificationService.findAllByPersonIdAndDelete(UUID.fromString(personIdString));
        if(!notificationList.isEmpty()) {
            simpMessagingTemplate.convertAndSendToUser(personIdString, "/private", notificationList);
        }
    }

    public void notifyFrontendUser(final String personidString, Notification notification) {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        simpMessagingTemplate.convertAndSendToUser(personidString, "/private", notificationList);
    }
}
