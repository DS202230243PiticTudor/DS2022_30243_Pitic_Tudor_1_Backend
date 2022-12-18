package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.Measurement;
import com.ds.management.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {
    List<Measurement> findMeasurementsByPersonOrderByCreatedDateAsc(Person person);
}
