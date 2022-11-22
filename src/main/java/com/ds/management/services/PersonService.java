package com.ds.management.services;

import com.ds.management.domain.dtos.PersonCreateDTO;
import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonUpdateDTO;
import com.ds.management.domain.entities.Person;
import com.ds.management.exception.domain.EmailExistException;
import com.ds.management.exception.domain.UserNotFoundException;
import com.ds.management.exception.domain.UsernameExistException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

public interface PersonService {
    String register(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException;
    List<PersonDeviceDTO> findAll();
    PersonDeviceDTO findByUsername(String username);
    PersonDeviceDTO findById(UUID id);
    PersonDeviceDTO findByEmail(String email);
    Person findEntityByUsername(String username);

    PersonDeviceDTO addNewPerson(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException;
    PersonDeviceDTO updatePerson(PersonUpdateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException, EntityNotFoundException;

    void deleteById(UUID id) throws EntityNotFoundException;
}
