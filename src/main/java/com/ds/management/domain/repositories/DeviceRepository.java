package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.Device;
import com.ds.management.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    @Override
    @Query("select i from Device i order by i.createdDate")
    List<Device> findAll();
    Optional<Device> findById(UUID id);

    List<Device> findDevicesByPerson(Person person);
    List<Device> findDevicesByPersonOrderByCreatedDateAsc(Person person);
}
