package com.globant.vividseats.repository;

import com.globant.vividseats.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
