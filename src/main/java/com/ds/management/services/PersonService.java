package com.ds.management.services;

import com.ds.management.domain.dtos.PersonCreateDTO;
import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.exception.domain.EmailExistException;
import com.ds.management.exception.domain.UserNotFoundException;
import com.ds.management.exception.domain.UsernameExistException;

import java.util.List;

public interface PersonService {
    String register(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException;
    List<PersonDeviceDTO> findAll();
    PersonDeviceDTO findByUsername(String username);
    PersonDeviceDTO findByEmail(String email);
}
