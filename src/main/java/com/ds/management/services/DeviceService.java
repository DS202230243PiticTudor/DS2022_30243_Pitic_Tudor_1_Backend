package com.ds.management.services;

import com.ds.management.domain.dtos.DeviceCreateDTO;
import com.ds.management.domain.dtos.DeviceDTO;
import com.ds.management.domain.dtos.DeviceUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    List<DeviceDTO> findAll();
    List<DeviceDTO> findAllByPersonId(UUID id);
    DeviceDTO findById(UUID id);
    DeviceDTO addNewDevice(DeviceCreateDTO dto);
    DeviceDTO updateDevice(DeviceUpdateDTO dto);
    void deleteById(UUID id);
}
