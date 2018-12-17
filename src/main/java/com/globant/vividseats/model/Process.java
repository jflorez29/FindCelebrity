package com.globant.vividseats.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Process implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String file;
    private Date processDate;
    private String result;
}
