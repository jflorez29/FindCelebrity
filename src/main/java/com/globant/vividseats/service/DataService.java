package com.globant.vividseats.service;

import com.globant.vividseats.model.Person;
import com.globant.vividseats.model.Process;
import com.globant.vividseats.repository.PersonRepository;
import com.globant.vividseats.repository.ProcessRepository;
import com.globant.vividseats.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Service to manage the repo operations like save, process and validate.
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
     * Process each line of data and decompose line with data of person and their relations.
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
     * Save person on database.
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
        Optional<List<Person>> optionalPeople = personRepository.findPeopleByProcess(process.getId());
        if (optionalPeople.isPresent()){
            List<Person> people = optionalPeople.get();
            people.forEach(person -> {
                Long[] idsKnown = person.getIdPersonKnow();
                Arrays.stream(idsKnown).forEach(idKnown -> {
                    Optional<Person> optionalPerson = personRepository.findPersonByIdAndProcess(idKnown, process.getId());
                    if (!optionalPerson.isPresent()){
                        savePerson(new Person(idKnown, process.getId()));
                    }
                });
            });
        }
    }

    /**
     * Create object with information about process.
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
     * Return optional of Process from database.
     * @param id process id
     * @return Optional of process
     */
    public Optional<Process> getProcessById(Long id){
        return processRepository.findById(id);
    }


    /**
     * Save process in database.
     * @param process
     * @return process after save in database
     */
    public Process saveProcess(Process process){
        return processRepository.save(process);
    }

    /**
     * Get people from repository by process Id.
     * @param processId
     * @return
     */
    public Optional<List<Person>> getPeopleByProcess(Long processId){
        return personRepository.findPeopleByProcess(processId);
    }


}
