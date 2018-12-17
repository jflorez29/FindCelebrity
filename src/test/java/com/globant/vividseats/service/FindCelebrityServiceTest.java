package com.globant.vividseats.service;

import com.globant.vividseats.model.Person;
import com.globant.vividseats.model.Process;
import com.globant.vividseats.repository.PersonRepository;
import com.globant.vividseats.repository.ProcessRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindCelebrityServiceTest {


    @Test
    public void whoIsCelebrity_whenCelebrityExists_ReturnOptionalWithCelebrity() {
        FileService fileService = mock(FileService.class);
        DataService dataService = mock(DataService.class);
        Optional<List<Person>> optional = Optional.empty();
        Long[] data = {10L};
        Person person1 = new Person(1L, new Long[]{2L,3L,4L}, 1L);
        Person person2 = new Person(2L, new Long[]{}, 1L);
        Person person3 = new Person(3L, new Long[]{2L,4L}, 1L);
        Person person4 = new Person(4L, new Long[]{2L,3L}, 1L);
        List<Person> people = Arrays.asList(person1,person2,person3,person4);
        optional = optional.of(people);
        when(dataService.getPeopleByProcess(1L)).thenReturn(optional.of(people));
        FindCelebrityService findCelebrityService = new FindCelebrityService(fileService, dataService);
        Process process = new Process();
        process.setId(1L);
        Optional<Person> optionalPerson = findCelebrityService.whoIsCelebrity(process);
        Assert.assertTrue(optionalPerson.isPresent());
        Person celebrity = optionalPerson.get();
        Assert.assertEquals(new Long(2), celebrity.getId());
    }

    @Test
    public void whoIsCelebrity_whenNotAllPeopleKnowPossibleCelebrity_ReturnOptionalEmpty() {
        FileService fileService = mock(FileService.class);
        DataService dataService = mock(DataService.class);
        Optional<List<Person>> optional = Optional.empty();
        Long[] data = {10L};
        Person person1 = new Person(1L, new Long[]{3L,4L}, 1L);
        Person person2 = new Person(2L, new Long[]{}, 1L);
        Person person3 = new Person(3L, new Long[]{2L,4L}, 1L);
        Person person4 = new Person(4L, new Long[]{2L,3L}, 1L);
        List<Person> people = Arrays.asList(person1,person2,person3,person4);
        optional = optional.of(people);
        when(dataService.getPeopleByProcess(1L)).thenReturn(optional.of(people));
        FindCelebrityService findCelebrityService = new FindCelebrityService(fileService, dataService);
        Process process = new Process();
        process.setId(1L);
        Optional<Person> optionalPerson = findCelebrityService.whoIsCelebrity(process);
        Assert.assertFalse(optionalPerson.isPresent());
    }
}