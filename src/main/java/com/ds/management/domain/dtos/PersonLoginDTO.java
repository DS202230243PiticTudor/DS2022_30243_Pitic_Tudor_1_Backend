package com.ds.management.domain.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PersonLoginDTO {
    private String username;
    private String password;
}
