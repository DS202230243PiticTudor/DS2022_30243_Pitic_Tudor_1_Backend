package com.ds.management.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueMessage {
    @JsonProperty("personId")
    private UUID personId;
    @JsonProperty("recordedDate")
    private Date recordedDate;
    @JsonProperty("deviceId")
    private UUID deviceId;
    @JsonProperty("energyConsumption")
    private double energyConsumption;
}
