package com.ds.management.domain.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class DeviceReadingPair {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;
    @Column(name = "device_id", nullable = false)
    private UUID deviceId;
    @Column(name = "reading", nullable = false)
    private double reading;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Measurement measurement;

}
