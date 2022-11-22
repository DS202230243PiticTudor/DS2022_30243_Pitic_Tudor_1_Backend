package com.ds.management.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PersonUpdateDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String newUsername;
    private String username;
    private String newEmail;
    private String email;
    boolean isActive;
    boolean isNotLocked;
}
