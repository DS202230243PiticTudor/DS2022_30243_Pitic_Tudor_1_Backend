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
    private String username;
    private String password;
    private String email;
    private String role;
    private String[] authorities;
}
