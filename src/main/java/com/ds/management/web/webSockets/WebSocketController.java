package com.ds.management.web.webSockets;

import com.ds.management.domain.dtos.Message;
import com.ds.management.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody Message message) {
        this.webSocketService.notifyFrontend(message.getMessageContent());
    }

}
