package com.ds.management.services.impl;

import com.ds.management.domain.dtos.DeviceCreateDTO;
import com.ds.management.domain.dtos.DeviceDTO;
import com.ds.management.domain.dtos.DeviceUpdateDTO;
import com.ds.management.domain.entities.Device;
import com.ds.management.domain.entities.Person;
import com.ds.management.domain.repositories.DeviceRepository;
import com.ds.management.domain.repositories.PersonRepository;
import com.ds.management.services.DeviceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    private final ModelMapper modelMapper;
    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;

    @Autowired
    public DeviceServiceImpl(
            DeviceRepository deviceRepository,
            ModelMapper modelMapper,
            PersonRepository personRepository
    ) {
        this.modelMapper = modelMapper;
        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<DeviceDTO> findAll() {
        return this.deviceRepository.findAll().stream()
                .map(device -> this.modelMapper.map(device, DeviceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDTO> findAllByPersonId(UUID id) {
        Optional<Person> personOptional = this.personRepository.findById(id);
        if(personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return this.deviceRepository.findDevicesByPersonOrderByCreatedDateAsc(personOptional.get()).stream()
                .map(device -> this.modelMapper.map(device, DeviceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO findById(UUID id) {
        Optional<Device> device = this.deviceRepository.findById(id);
        if(device.isEmpty()) {
            throw new EntityNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return this.modelMapper.map(device.get(), DeviceDTO.class);
    }

    @Override
    public DeviceDTO addNewDevice(DeviceCreateDTO dto) {
        Optional<Person> personOptional = this.personRepository.findById(dto.getPersonId());
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + dto.getPersonId());
        }
        Device device = Device.builder()
                .description(dto.getDescription())
                .createdDate(new Date())
                .address(dto.getAddress())
                .maxEnergyConsumption(dto.getMaxEnergyConsumption())
                .person(personOptional.get())
                .build();
        this.deviceRepository.save(device);
        return this.modelMapper.map(device, DeviceDTO.class);
    }

    @Override
    public DeviceDTO updateDevice(DeviceUpdateDTO dto) {
        Optional<Device> deviceOriginal = this.deviceRepository.findById(dto.getId());
        if(deviceOriginal.isEmpty()) {
            throw new EntityNotFoundException(Device.class.getSimpleName() + " with id: " + dto.getId());
        }
        Optional<Person> personOptional = this.personRepository.findById(dto.getPersonId());
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + dto.getPersonId());
        }
        Device device = Device.builder()
                .id(deviceOriginal.get().getId())
                .createdDate(deviceOriginal.get().getCreatedDate())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .maxEnergyConsumption(dto.getMaxEnergyConsumption())
                .person(personOptional.get())
                .build();
        this.deviceRepository.save(device);
        return this.modelMapper.map(device, DeviceDTO.class);
    }

    @Override
    public void deleteById(UUID id) {
        Optional<Device> device = this.deviceRepository.findById(id);
        if(device.isEmpty()) {
            throw new EntityNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        this.deviceRepository.deleteById(id);
    }
}
