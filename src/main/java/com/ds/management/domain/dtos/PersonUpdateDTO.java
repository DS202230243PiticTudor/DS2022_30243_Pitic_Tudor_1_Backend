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
    private String username;
    private String password;
    private String email;
}
