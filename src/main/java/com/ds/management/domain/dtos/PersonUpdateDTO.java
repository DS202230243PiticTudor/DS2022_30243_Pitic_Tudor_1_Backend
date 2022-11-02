package com.ds.management.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PersonUpdateDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
//    private String role;
}
