package com.ds.management.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PersonDeviceDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
//    private String role;
}
