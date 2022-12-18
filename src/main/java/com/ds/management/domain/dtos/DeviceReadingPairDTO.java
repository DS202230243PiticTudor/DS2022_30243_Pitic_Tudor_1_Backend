package com.ds.management.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeviceReadingPairDTO {
    private UUID id;
    private UUID deviceId;
    private double reading;
}
