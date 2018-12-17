package com.globant.vividseats.service;

import com.globant.vividseats.exception.DataFormatException;
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
     */
    public HttpEntity process(MultipartFile multipartFile) throws IOException, DataFormatException {
        Process process = new Process();
        try {
            File file = fileService.loadFile(multipartFile);
            process = dataBaseService.createProcess(file.getName());
            List<String> data = fileService.readData(file);
            dataBaseService.loadDataFromFile(data, process);
            Optional<Person> celebrity = dataBaseService.whoIsCelebrity();
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
}
