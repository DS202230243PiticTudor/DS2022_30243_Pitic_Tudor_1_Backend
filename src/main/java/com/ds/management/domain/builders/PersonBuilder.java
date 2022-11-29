package com.ds.management.domain.builders;

import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonBuilder {
    public PersonDeviceDTO toDTO(Person person) {
        return PersonDeviceDTO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .username(person.getUsername())
                .email(person.getEmail())
                .role(person.getRole())
                .isNotLocked(person.isNotLocked())
                .isActive(person.isActive())
                .build();
    }

}
