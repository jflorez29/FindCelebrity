package com.globant.vividseats.service;

import com.globant.vividseats.exception.DataFormatException;
import com.globant.vividseats.exception.FileEmptyException;
import com.globant.vividseats.model.Person;
import com.globant.vividseats.model.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class FindCelebrityService {

    private final FileService fileService;
    private final DataService dataBaseService;

    @Autowired
    public FindCelebrityService(FileService fileService, DataService dataBaseService) {
        this.fileService = fileService;
        this.dataBaseService = dataBaseService;
    }

    /**
     * Main method to process file and return if celebrity exists or not
     * @param multipartFile
     * @return Http Response about result of process
     * @throws IOException
     * @throws DataFormatException
     * @throws FileEmptyException
     */
    public HttpEntity process(MultipartFile multipartFile) throws IOException, DataFormatException, FileEmptyException {
        Process process = new Process();
        try {
            File file = fileService.loadFile(multipartFile);
            process = dataBaseService.createProcess(file.getName());
            List<String> data = fileService.readData(file);
            dataBaseService.loadDataFromFile(data, process);
            Optional<Person> celebrity = whoIsCelebrity(process);
            if (celebrity.isPresent()){
                process.setResult("The celebrity is "+ celebrity.get().getId());
            }else{
                process.setResult("Not found any celebrity");
            }
            dataBaseService.saveProcess(process);
            return ResponseEntity.ok(process);
        } catch (IOException e) {
            process.setResult(e.getMessage());
            dataBaseService.saveProcess(process);
            throw new IOException(e);
        } catch (DataFormatException e) {
            process.setResult(e.getMessage());
            dataBaseService.saveProcess(process);
            throw new DataFormatException(e);
        } catch (FileEmptyException e) {
            process.setResult(e.getMessage());
            dataBaseService.saveProcess(process);
            throw new FileEmptyException(e);
        }
    }

    /**
     * Return result of process from database
     * @param idProcess Process id
     * @return if process exists return it otherwise Status NOT FOUND
     */
    public HttpEntity getResultFromProcess(Long idProcess){
        Optional<Process> optionalProcess = dataBaseService.getProcessById(idProcess);
        if (optionalProcess.isPresent()){
            return ResponseEntity.ok(optionalProcess.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Not found process with id %s", idProcess));
        }
    }


    /**
     * Algorithm where search celebrity among people.
     *
     * Add all of people to a stack and compare two of them from top while stack contains more of one person,
     * finally validate if all of people know possible celebrity to confirm as the celebrity
     * @param process current process
     * @return Optional Person, when there is a celebrity, assign it to optional
     */
    public Optional<Person> whoIsCelebrity(Process process){
        Optional<Person> celebrity = Optional.empty();
        Optional<List<Person>> optionalPeople = dataBaseService.getPeopleByProcess(process.getId());
        if (optionalPeople.isPresent()){
            List<Person> people = optionalPeople.get();
            Stack<Person> stack = new Stack<>();
            stack.addAll(people);
            while (stack.size() > 1){
                Person personA = stack.pop();
                Person personB = stack.pop();
                Person possibleCelebrity = possibleCelebrity(personA, personB);
                stack.push(possibleCelebrity);
            }

            Person possibleCelebrity = stack.pop();
            boolean confirmation = people.stream()
                    .allMatch(person -> {
                        if (person != possibleCelebrity){
                            return person.knows(possibleCelebrity);
                        }
                        return true;
                    });
            if (confirmation){
                celebrity = Optional.of(possibleCelebrity);
            }
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
