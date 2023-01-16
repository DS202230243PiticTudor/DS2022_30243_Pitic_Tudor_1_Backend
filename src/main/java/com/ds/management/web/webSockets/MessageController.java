package com.ds.management.web.webSockets;

import com.ds.management.domain.dtos.Message;
import com.ds.management.domain.dtos.ResponseMessage;
import com.ds.management.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageController {

    WebSocketService webSocketService;

    @Autowired
    public MessageController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/message") // ws/messages - receiving endpoint (this is where the frontend sends messages)
    @SendTo("/topic/messages") // departure endpoint (this is where the frontend listens)
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message") // ws/private-messages - receiving endpoint (this is where the frontend sends messages)
    @SendToUser("/topic/private-messages") // departure endpoint (this is where the frontend listens)
    public ResponseMessage getPrivateMessage(final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message-notification")
    public void receiveSignalAndSendNotification(final Message message) {
        String personId = message.getMessageContent();
        this.webSocketService.notifyFrontendUserAndDeleteNotifications(personId);
    }
}
