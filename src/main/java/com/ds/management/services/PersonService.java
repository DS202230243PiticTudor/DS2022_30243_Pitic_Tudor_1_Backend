package com.ds.management.services;

import com.ds.management.domain.builders.PersonBuilder;
import com.ds.management.domain.dtos.PersonCreateDTO;
import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonUpdateDTO;
import com.ds.management.domain.entities.Person;
import com.ds.management.domain.repositories.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PersonBuilder builder;

    @Autowired
    public PersonService(PersonRepository personRepository, ModelMapper modelMapper, PersonBuilder builder) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.builder = builder;
    }

    @Transactional(readOnly = true)
    public List<PersonDeviceDTO> findAll() {
        List<Person> items = personRepository.findAll();
        return items.stream().map(builder::toDTO).collect(Collectors.toList());
    }

    public ResponseEntity<PersonDeviceDTO> findById(UUID id) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(id);
        if(personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        PersonDeviceDTO dto = this.modelMapper.map(personOptional.get(), PersonDeviceDTO.class);
        return ResponseEntity.ok().body(dto);
    }

    public ResponseEntity<String> create(PersonCreateDTO dto) {
        Person person = this.modelMapper.map(dto, Person.class);
        person.setCreatedDate(new Date(System.currentTimeMillis()));
        UUID personId = personRepository.save(person).getId();
        return ResponseEntity.ok().body(personId.toString());
    }

    public ResponseEntity<PersonDeviceDTO> update(PersonUpdateDTO dto) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(dto.getId());
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + dto.getId());
        }
        Person person = builder.toEntityUpdate(dto);
        person.setId(dto.getId());
        person.setCreatedDate(personOptional.get().getCreatedDate());
        personRepository.save(person);
        return ResponseEntity.ok().body(this.builder.toDTO(person));
    }

    public ResponseEntity<Boolean> deleteById(UUID id) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        personRepository.deleteById(id);
        return ResponseEntity.ok().body(true);
    }
}
