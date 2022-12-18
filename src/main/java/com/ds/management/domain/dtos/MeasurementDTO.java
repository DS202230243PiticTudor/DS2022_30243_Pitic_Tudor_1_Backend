package com.ds.management.domain.dtos;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MeasurementDTO {
    private UUID id;
    private Date createdDate;
    private double totalMeasurement;
    private List<DeviceReadingPairDTO> deviceReadingPairs;
}
