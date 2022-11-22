package com.ds.management.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDeviceDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private boolean isActive;
    private boolean isNotLocked;
}
