package com.ds.management.web.controllers;

import com.ds.management.domain.dtos.PersonCreateDTO;
import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonUpdateDTO;
import com.ds.management.domain.entities.Person;
import com.ds.management.exception.domain.EmailExistException;
import com.ds.management.exception.domain.ExceptionHandling;
import com.ds.management.exception.domain.UserNotFoundException;
import com.ds.management.exception.domain.UsernameExistException;
import com.ds.management.services.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = {"/persons"})
public class PersonController extends ExceptionHandling {
    private final PersonServiceImpl personService;

    @Autowired
    public  PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('user:read')")
    public List<PersonDeviceDTO> getAll() {
        return this.personService.findAll();
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<PersonDeviceDTO> getItem(@PathVariable("id") UUID id) {
        PersonDeviceDTO dto = this.personService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<PersonDeviceDTO> create(@RequestBody PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException {
        PersonDeviceDTO personDeviceDTO = this.personService.addNewPerson(dto);
        return ResponseEntity.ok().body(personDeviceDTO);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public void delete(@PathVariable("id") UUID id) {
        this.personService.deleteById(id);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<PersonDeviceDTO> update(@RequestBody PersonUpdateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException, EntityNotFoundException {
        PersonDeviceDTO personDeviceDTO = this.personService.updatePerson(dto);
        return ResponseEntity.ok().body(personDeviceDTO);
    }

   @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException {
        String id = personService.register(dto);
        return ResponseEntity.ok().body(id);
   }
}
