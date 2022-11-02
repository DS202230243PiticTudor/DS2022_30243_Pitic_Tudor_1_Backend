package com.ds.management.domain.dtos;

import lombok.*;

@Data
@Builder
public class PersonCreateDTO {
    private String username;
    private String password;
    private String email;
    // private Role role;
}
