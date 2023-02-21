package com.ds.management.web.webSockets;

import com.ds.management.domain.dtos.ChatMessageDTO;
import com.ds.management.domain.dtos.ChatMessageSendDTO;
import com.ds.management.domain.dtos.SimpleMessage;
import com.ds.management.services.ChatService;
import com.ds.management.services.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class MessageController {

    private final WebSocketService webSocketService;
    private final ChatService chatService;
    private final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    public MessageController(WebSocketService webSocketService, ChatService chatService) {
        this.webSocketService = webSocketService;
        this.chatService = chatService;
    }

    @MessageMapping("/private-message-notification")
    public void receiveSignalAndSendNotification(final SimpleMessage simpleMessage) {
        String personId = simpleMessage.getMessageContent();
        this.webSocketService.notifyFrontendUserAndDeleteNotifications(personId);
    }

    @MessageMapping("/send-message")
    public void sendMessage(ChatMessageSendDTO chatMessageSendDTO) {
        ChatMessageDTO chatMessageDTO = this.chatService.sendMessage(chatMessageSendDTO);
        this.webSocketService.sendMessageToUser(chatMessageSendDTO.getSenderId(), chatMessageDTO);
    }
}
