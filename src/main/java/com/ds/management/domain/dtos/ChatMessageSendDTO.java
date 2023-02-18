package com.ds.management.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageSendDTO {
    private UUID senderId;
    private UUID peerId;
    private String content;
}
