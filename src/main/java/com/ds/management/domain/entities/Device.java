package com.ds.management.domain.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Device {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "maxEnergyConsumption", nullable = false)
    private float maxEnergyConsumption;
    @Column(name = "avgConsumption", nullable = false)
    private float avgConsumption;
}
