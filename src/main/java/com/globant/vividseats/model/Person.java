package com.globant.vividseats.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Getter
@Setter
public class Person implements Serializable {
    @Id
    private Long id;
    private Long process;
    private Long[] idPersonKnow = {};

    public Person() {
    }

    public Person(Long id, Long process) {
        this.id = id;
        this.process = process;
    }

    public Person(Long id, Long[] idPersonKnow, Long process) {
        this.id = id;
        this.idPersonKnow = idPersonKnow;
        this.process = process;
    }

    public Boolean knows(Person person){
        return Arrays.stream(idPersonKnow).anyMatch(id -> person.getId().equals(id));
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                '}';
    }
}
