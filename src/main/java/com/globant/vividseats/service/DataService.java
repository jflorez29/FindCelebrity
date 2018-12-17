package com.globant.vividseats.service;

import com.globant.vividseats.model.Person;
import com.globant.vividseats.model.Process;
import com.globant.vividseats.repository.PersonRepository;
import com.globant.vividseats.repository.ProcessRepository;
import com.globant.vividseats.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;


/**
 * Service to manage all of date of application in operations like save, process and validate.
 */
@Service
public class DataService {

    private final PersonRepository personRepository;
    private final ProcessRepository processRepository;

    @Autowired
    public DataService(PersonRepository personRepository, ProcessRepository processRepository){
        this.personRepository = personRepository;
        this.processRepository = processRepository;
    }

    /**
     * Process each line of data and decompose line with data of person and their relations
     * @param data List of string from file
     * @param process current process
     */
    public void loadDataFromFile(List<String> data, Process process){
        data.forEach(line -> {
            line = line.replace(Constants.END_SIGN, "");
            String lineId = line.split(Constants.SEPARATOR_ID)[0];
            String lineRelations = line.split(Constants.SEPARATOR_ID)[1];
            String[] arrayRelations = lineRelations.split(Constants.SEPARATOR_RELATIONSHIP);
            Long[] idKnown = Arrays.stream(arrayRelations).map(Long::valueOf).toArray(Long[]::new);
            Long id = Long.valueOf(lineId);
            Person person = new Person(id, idKnown, process.getId());
            savePerson(person);
        });
        validateData(process);
    }

    /**
     * Save person on database
     * @param person
     * @return person after saved on database
     */
    private Person savePerson(Person person){
        person = personRepository.save(person);
        return person;
    }

    /**
     * Validate integrity of the processed data to confirm whether all persons exist (including those in relationships) on the database,
     * if they are not, then created.
     * @param process process in curse
     */
    private void validateData(Process process){
        Iterable<Person> people = personRepository.findAll();
        people.forEach(person -> {
            Long[] idsKnown = person.getIdPersonKnow();
            Arrays.stream(idsKnown).forEach(idKnown -> {
                Optional<Person> optionalPerson = personRepository.findById(idKnown);
                if (!optionalPerson.isPresent()){
                    savePerson(new Person(idKnown, process.getId()));
                }
            });
        });
    }

    /**
     * Create object with information about process
     * @param fileName name of file uses in process
     * @return Process with information
     */
    public Process createProcess(String fileName){
        Process process = new Process();
        process.setFile(fileName);
        process.setProcessDate(new Date());
        process = saveProcess(process);
        return process;
    }

    /**
     * Return optional of Process from database
     * @param id process id
     * @return Optional of process
     */
    public Optional<Process> getProcessById(Long id){
        return processRepository.findById(id);
    }


    /**
     * Save process in database
     * @param process
     * @return process after save in database
     */
    public Process saveProcess(Process process){
        return processRepository.save(process);
    }

    /**
     * Algorithm where search celebrity among people.
     *
     * Add all of people to a stack and compare two of them from top while stack contains more of one person,
     * finally validate if all of people know possible celebrity to confirm as the celebrity
     * @return Optional Person, when there is a celebrity, assign it to optional
     */
    public Optional<Person> whoIsCelebrity(){
        Optional<Person> celebrity = Optional.empty();
        Iterable<Person> people = personRepository.findAll();
        Stack<Person> stack = new Stack<>();
        people.forEach(stack::add);

        while (stack.size() > 1){
            Person personA = stack.pop();
            Person personB = stack.pop();
            Person possibleCelebrity = possibleCelebrity(personA, personB);
            stack.push(possibleCelebrity);
        }

        Person possibleCelebrity = stack.pop();
        boolean confirmation = StreamSupport.stream(people.spliterator(), false)
                .allMatch(person -> {
                    if (person != possibleCelebrity){
                        return person.knows(possibleCelebrity);
                    }
                    return true;
                });
        if (confirmation){
            celebrity = Optional.of(possibleCelebrity);
        }
        return celebrity;
    }

    /**
     * Return possible Celebrity between two candidates
     * @param personA First person of Stack
     * @param personB Second person of stack
     * @return PersonA when PersonA not knows PersonB else PersonB
     */
    private Person possibleCelebrity(Person personA, Person personB){
        if (personA.knows(personB)){
            return personB;
        }else {
            return personA;
        }
    }
}
