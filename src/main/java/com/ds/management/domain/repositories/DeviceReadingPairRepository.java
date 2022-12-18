package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.DeviceReadingPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceReadingPairRepository extends JpaRepository<DeviceReadingPair, UUID> {
}
