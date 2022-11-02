package com.ds.management.web.controllers;

import com.ds.management.domain.dtos.PersonCreateDTO;
import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonUpdateDTO;
import com.ds.management.domain.entities.Person;
import com.ds.management.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public  PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<PersonDeviceDTO> getAll() {
        return this.personService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getItem(@PathVariable("id") UUID id) {
        try {
            return personService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping()
    public ResponseEntity<String> create(@RequestBody PersonCreateDTO dto) {
        try {
            return personService.create(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Couldn't create new " + Person.class.getSimpleName());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        try {
            return personService.deleteById(id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<PersonDeviceDTO> update(@RequestBody PersonUpdateDTO dto) {
        try {
            return personService.update(dto);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
