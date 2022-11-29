package com.ds.management.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUpdateDTO {
    private UUID id;
    private String description;
    private String address;
    private float maxEnergyConsumption;
    private UUID personId;
}
