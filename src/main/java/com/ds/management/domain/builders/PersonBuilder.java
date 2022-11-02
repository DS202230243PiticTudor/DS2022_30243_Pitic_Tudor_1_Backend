package com.ds.management.domain.builders;

import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonUpdateDTO;
import com.ds.management.domain.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonBuilder {
    public PersonDeviceDTO toDTO(Person person) {
        return PersonDeviceDTO.builder()
                .id(person.getId())
                .username(person.getUsername())
                .email(person.getEmail())
                .password(person.getPassword())
                .build();
    }

    public Person toEntityUpdate(PersonUpdateDTO dto) {
        return Person.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

}
