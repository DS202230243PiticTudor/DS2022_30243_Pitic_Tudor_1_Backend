package com.ds.management.services.impl;

import com.ds.management.domain.builders.PersonBuilder;
import com.ds.management.domain.dtos.*;
import com.ds.management.domain.entities.*;
import com.ds.management.domain.enumeration.Role;
import com.ds.management.domain.repositories.DeviceReadingPairRepository;
import com.ds.management.domain.repositories.MeasurementRepository;
import com.ds.management.domain.repositories.PersonRepository;
import com.ds.management.exception.domain.EmailExistException;
import com.ds.management.exception.domain.UserNotFoundException;
import com.ds.management.exception.domain.UsernameExistException;
import com.ds.management.services.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("personDetailsService")
public class PersonServiceImpl implements PersonService, UserDetailsService {
    public static final String NO_PERSON_FOUND_BY_USERNAME = "No person found by username: ";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";

    private DeviceServiceImpl deviceService;
    private final PersonRepository personRepository;
    private final DeviceReadingPairRepository deviceReadingPairRepository;
    private final MeasurementRepository measurementRepository;
    private final ModelMapper modelMapper;
    private final PersonBuilder builder;
    private BCryptPasswordEncoder passwordEncoder;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public PersonServiceImpl(
            DeviceServiceImpl deviceService,
            PersonRepository personRepository,
            DeviceReadingPairRepository deviceReadingPairRepository,
            MeasurementRepository measurementRepository,
            ModelMapper modelMapper,
            PersonBuilder builder,
            BCryptPasswordEncoder passwordEncoder
            ) {
        this.deviceService = deviceService;
        this.personRepository = personRepository;
        this.deviceReadingPairRepository = deviceReadingPairRepository;
        this.measurementRepository = measurementRepository;
        this.modelMapper = modelMapper;
        this.builder = builder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personOptional = this.personRepository.findPersonByUsername(username);
        if(personOptional.isEmpty()) {
            LOGGER.error(NO_PERSON_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_PERSON_FOUND_BY_USERNAME + username);
        } else {
            return new UserPrincipal(personOptional.get());
        }
    }

    @Override
    public String register(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, dto.getUsername(), dto.getEmail());
        Person person = this.modelMapper.map(dto, Person.class);
        person.setCreatedDate(new Date(System.currentTimeMillis()));
        String encodedPassword = encodePassword(person.getPassword());
        person.setPassword(encodedPassword);
        person.setActive(true);
        person.setNotLocked(true);
        person.setRole(dto.getRole());
        person.setAuthorities(getRoleEnumName(dto.getRole()).getAuthorities());
        person.setDevices(new LinkedList<>());
        person.setMeasurements(new LinkedList<>());
        personRepository.save(person);
        LOGGER.info("New user password: " + dto.getPassword());
        return person.getId().toString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private PersonDeviceDTO validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, EmailExistException, UsernameExistException {
        PersonDeviceDTO personByNewUsername = findByUsername(newUsername);
        PersonDeviceDTO personByNewEmail = findByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            PersonDeviceDTO currentPerson = findByUsername(currentUsername);
            if (currentPerson == null) {
                throw new UserNotFoundException(NO_PERSON_FOUND_BY_USERNAME + currentUsername);
            }
            if (personByNewUsername != null && !currentPerson.getId().equals(personByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (personByNewEmail != null && !currentPerson.getId().equals(personByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentPerson;
        } else {
            if (personByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (personByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PersonDeviceDTO> findAll() {
        List<Person> items = personRepository.findAll();
        return items.stream().map(builder::toDTO).collect(Collectors.toList());
    }

    @Override
    public PersonDeviceDTO findByUsername(String username) {
        Optional<Person> optional = this.personRepository.findPersonByUsername(username);
        if(optional.isEmpty()) {
            return null;
        }
        return this.modelMapper.map(optional.get(), PersonDeviceDTO.class);
    }

    @Override
    public Person findEntityByUsername(String username) {
        Optional<Person> optional = this.personRepository.findPersonByUsername(username);
        if(optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public PersonDeviceDTO addNewPerson(PersonCreateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, dto.getUsername(), dto.getEmail());
        Person person = this.modelMapper.map(dto, Person.class);
        person.setCreatedDate(new Date(System.currentTimeMillis()));
        String encodedPassword = encodePassword(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole(getRoleEnumName(dto.getRole()).name());
        person.setAuthorities(getRoleEnumName(dto.getRole()).getAuthorities());
        person.setDevices(new LinkedList<>());
        person.setMeasurements(new LinkedList<>());
        personRepository.save(person);
        return this.modelMapper.map(person, PersonDeviceDTO.class);
    }

    @Override
    public PersonDeviceDTO updatePerson(PersonUpdateDTO dto) throws UserNotFoundException, EmailExistException, UsernameExistException, EntityNotFoundException {
        validateNewUsernameAndEmail(dto.getUsername(), dto.getNewUsername(), dto.getNewEmail());
        Optional<Person> personOptional = this.personRepository.findById(dto.getId());
        if(personOptional.isEmpty()){
            throw new EntityNotFoundException();
        }
        Person person = Person.builder()
                        .id(personOptional.get().getId())
                        .username(dto.getNewUsername())
                        .email(dto.getNewEmail())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                                .build();
        person.setActive(dto.isActive());
        person.setNotLocked(dto.isNotLocked());
        person.setId(personOptional.get().getId());
        person.setCreatedDate(personOptional.get().getCreatedDate());
        person.setPassword(personOptional.get().getPassword());
        person.setRole(personOptional.get().getRole());
        person.setAuthorities(personOptional.get().getAuthorities());
        person.setDevices(personOptional.get().getDevices());
        person.setMeasurements(personOptional.get().getMeasurements());
        personRepository.save(person);
        return this.modelMapper.map(person, PersonDeviceDTO.class);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role);
    }

    @Override
    public PersonDeviceDTO findByEmail(String email) {
        Optional<Person> optional = this.personRepository.findPersonByEmail(email);
        if(optional.isEmpty()) {
            return null;
        }
        return this.modelMapper.map(optional.get(), PersonDeviceDTO.class);
    }

    @Override
    public PersonDeviceDTO findById(UUID id) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(id);
        if(personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return this.modelMapper.map(personOptional.get(), PersonDeviceDTO.class);
    }

    @Override
    public void deleteById(UUID id) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
//        personOptional.get().getDevices().forEach(device -> this.deviceService.deleteById(device.getId()));
        personRepository.deleteById(id);
    }

    @Override
    public List<DeviceDTO> getPersonDevices(UUID personId) {
        Optional<Person> personOptional = this.personRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + personId);
        }
        return personOptional.get().getDevices().stream()
                .map(device -> this.modelMapper.map(device, DeviceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void makeMeasurement(UUID personId) {
        Optional<Person> personOptional = this.personRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + personId);
        }
        Person person = personOptional.get();
        List<Device> devices = person.getDevices();
        List<Measurement> measurements = person.getMeasurements();
        List<DeviceReadingPair> pairs = new LinkedList<>();
        DeviceReadingPair pair;
        Measurement measurement = Measurement.builder()
                .createdDate(new Date())
                .totalMeasurement(0.0)
                .person(person)
                .build();
        double totalReading = 0;
        double currentPairReading;
        for (Device device : devices) {
            currentPairReading = Math.random() * device.getMaxEnergyConsumption();
            totalReading += currentPairReading;
            pair = DeviceReadingPair.builder()
                    .deviceId(device.getId())
                    .reading(currentPairReading)
                    .measurement(measurement)
                    .build();
            this.deviceReadingPairRepository.save(pair);
            pairs.add(pair);
        }
        measurement.setTotalMeasurement(totalReading);
        measurement.setDeviceReadingPairs(pairs);
        measurements.add(measurement);
        this.measurementRepository.save(measurement);
        person.setMeasurements(measurements);
        this.personRepository.save(person);
    }

    public void makeMeasurementWithDeviceReadingPairList(UUID personId, List<DeviceReadingPair> pairs, Date createdDate) {
        Optional<Person> personOptional = this.personRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + personId);
        }
        Person person = personOptional.get();
        List<Measurement> measurements = person.getMeasurements();
        double totalReading = 0;
        for(DeviceReadingPair pair : pairs) {
            totalReading += pair.getReading();
            this.deviceReadingPairRepository.save(pair);
        }
        Measurement measurement = Measurement.builder()
                .createdDate(createdDate)
                .totalMeasurement(totalReading)
                .person(person)
                .deviceReadingPairs(pairs)
                .build();
        this.measurementRepository.save(measurement);
        person.setMeasurements(measurements);
        this.personRepository.save(person);
    }

    // to dto baby
    @Override
    public List<MeasurementDTO> getMeasurements(UUID personId) {
        Optional<Person> personOptional = this.personRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + personId);
        }
//        List<Measurement> measurements = personOptional.get().getMeasurements();
        List<Measurement> measurements = measurementRepository.findMeasurementsByPersonOrderByCreatedDateAsc(personOptional.get());
        List<MeasurementDTO> measurementDTOS = new LinkedList<>();
        List<DeviceReadingPairDTO> deviceReadingPairDTOS;
        for (Measurement measurement : measurements) {
            deviceReadingPairDTOS = new LinkedList<>();
            for(DeviceReadingPair deviceReadingPair : measurement.getDeviceReadingPairs()) {
                deviceReadingPairDTOS.add(
                        DeviceReadingPairDTO.builder()
                                .id(deviceReadingPair.getId())
                                .deviceId(deviceReadingPair.getDeviceId())
                                .reading(deviceReadingPair.getReading())
                                .build()
                );
            }
            measurementDTOS.add(
                    MeasurementDTO.builder()
                    .id(measurement.getId())
                    .createdDate(measurement.getCreatedDate())
                    .totalMeasurement(measurement.getTotalMeasurement())
                    .deviceReadingPairs(deviceReadingPairDTOS)
                    .build()
            );
        }
        return measurementDTOS;
    }
}
