package com.ds.management.web.controllers;


import com.ds.management.domain.dtos.ChatMessageDTO;
import com.ds.management.domain.dtos.IndividualChatRequestDTO;
import com.ds.management.exception.domain.ConnectionToSamePersonException;
import com.ds.management.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = {"/chat"})
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{id}")
    public List<UUID> getIndividualChatsIds(@PathVariable("id") UUID personId) {
        return this.chatService.getIndividualChatsIds(personId);
    }

    @PostMapping("/connect-to-chat")
    public List<ChatMessageDTO> onConnected(@RequestBody IndividualChatRequestDTO dto) throws ConnectionToSamePersonException {
        return this.chatService.onConnected(dto);
    }
}
