package com.ds.management.services;

import com.ds.management.domain.dtos.*;
import com.ds.management.domain.entities.DeviceReadingPair;
import com.ds.management.domain.entities.Person;
import com.ds.management.exception.domain.EmailExistException;
import com.ds.management.exception.domain.UserNotFoundException;
import com.ds.management.exception.domain.UsernameExistException;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PersonService {
    String register(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException;
    List<PersonDeviceDTO> findAll();
    List<PersonDeviceDTO> findAllExceptWithId(UUID id);
    PersonDeviceDTO findByUsername(String username);
    PersonDeviceDTO findById(UUID id);
    PersonDeviceDTO findByEmail(String email);
    Person findEntityByUsername(String username);

    PersonDeviceDTO addNewPerson(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException;
    PersonDeviceDTO updatePerson(PersonUpdateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException, EntityNotFoundException;

    void deleteById(UUID id) throws EntityNotFoundException;
    List<DeviceDTO> getPersonDevices(UUID personId);
    void makeMeasurement(UUID personId);
    void makeMeasurementWithDeviceReadingPairList(UUID personId, List<DeviceReadingPair> pairs, Date createdDate);
    List<MeasurementDTO> getMeasurements(UUID personId);
}
