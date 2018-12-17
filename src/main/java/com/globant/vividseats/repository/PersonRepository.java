package com.globant.vividseats.repository;

import com.globant.vividseats.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<List<Person>> findPeopleByProcess(Long process);

    Optional<Person> findPersonByIdAndProcess(Long id, Long process);
}
