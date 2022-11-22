package com.ds.management.domain.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PersonCreateDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String role;
    boolean isNotLocked;
    boolean isActive;
}
