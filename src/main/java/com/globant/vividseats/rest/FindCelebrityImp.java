package com.globant.vividseats.rest;

import com.globant.vividseats.exception.DataFormatException;
import com.globant.vividseats.exception.FileEmptyException;
import com.globant.vividseats.service.FindCelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/find-celebrity")
public class FindCelebrityImp implements FindCelebrityRest {

    private FindCelebrityService findCelebrityService;

    @Autowired
    public FindCelebrityImp(FindCelebrityService findCelebrityService){
        this.findCelebrityService = findCelebrityService;
    }

    @Override
    @PostMapping(value = "/process")
    public HttpEntity findCelebrityProcess(@RequestParam("file") MultipartFile file) throws IOException, DataFormatException, FileEmptyException {
        return findCelebrityService.process(file);
    }

    @Override
    @GetMapping(value = "/process/{id-process:\\d}")
    public HttpEntity getResultProcess(@PathVariable("id-process")Long idProcess) {
        return findCelebrityService.getResultFromProcess(idProcess);
    }
}
