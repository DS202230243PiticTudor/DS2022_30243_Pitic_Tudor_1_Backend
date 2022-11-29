package com.ds.management.web.controllers;

import com.ds.management.domain.dtos.DeviceCreateDTO;
import com.ds.management.domain.dtos.DeviceDTO;
import com.ds.management.domain.dtos.DeviceUpdateDTO;
import com.ds.management.exception.domain.ExceptionHandling;
import com.ds.management.services.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = {"/devices"})
public class DeviceController extends ExceptionHandling {
    private final DeviceServiceImpl deviceService;

    @Autowired
    public DeviceController(DeviceServiceImpl deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('device:read')")
    public List<DeviceDTO> getAll() {
        return this.deviceService.findAll();
    }

    @GetMapping("/person/{id}")
    @PreAuthorize("hasAnyAuthority('device:read')")
    public List<DeviceDTO> getAllByPersonId(@PathVariable("id") UUID id) {
        return this.deviceService.findAllByPersonId(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('device:read')")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID id) {
        DeviceDTO dto = this.deviceService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('device:create')")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceCreateDTO deviceCreateDTO) {
        DeviceDTO dto = this.deviceService.addNewDevice(deviceCreateDTO);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('device:update')")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceUpdateDTO deviceUpdateDTO) {
        DeviceDTO dto = this.deviceService.updateDevice(deviceUpdateDTO);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('device:delete')")
    public void delete(@PathVariable("id") UUID id) {
        this.deviceService.deleteById(id);
    }
}
