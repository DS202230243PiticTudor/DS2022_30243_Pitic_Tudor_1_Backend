package com.ds.management.domain.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private UUID recipientId;
    private String content;
    private LocalDateTime sentAt;
    private boolean seen;
}
