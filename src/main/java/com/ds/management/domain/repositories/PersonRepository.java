package com.ds.management.domain.repositories;

import com.ds.management.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Override
    @Query("select i from Person i order by i.createdDate")
    List<Person> findAll();
    Optional<Person> findById(UUID id);
}
