package com.ds.management.services;

import com.ds.management.domain.dtos.ChatMessageDTO;
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
    private final NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate, NotificationService notificationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontendUserAndDeleteNotifications(final String personIdString) {
        List<Notification> notificationList = this.notificationService.findAllByPersonIdAndDelete(UUID.fromString(personIdString));
        if(!notificationList.isEmpty()) {
            simpMessagingTemplate.convertAndSendToUser(personIdString, "/private", notificationList);
        }
    }

    public void notifyFrontendUser(final String personIdString, Notification notification) {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        simpMessagingTemplate.convertAndSendToUser(personIdString, "/private", notificationList);
    }

    public void sendMessageToUser(UUID senderId, ChatMessageDTO chatMessageDTO) {
        List<ChatMessageDTO> chatMessageDTOList = new ArrayList<>();
        chatMessageDTOList.add(chatMessageDTO);
        simpMessagingTemplate.convertAndSendToUser(senderId.toString(), "/chat", chatMessageDTOList);
        simpMessagingTemplate.convertAndSendToUser(chatMessageDTO.getRecipientId().toString(), "/chat", chatMessageDTOList);
    }
}
