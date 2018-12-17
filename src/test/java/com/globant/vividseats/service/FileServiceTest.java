package com.globant.vividseats.service;

import com.globant.vividseats.exception.DataFormatException;
import com.globant.vividseats.exception.FileEmptyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    FileService fileService;
    String uploadPath = "C:/uploads/";

    @Before
    public void setUp(){
        fileService = new FileService(uploadPath);
    }


    @Test(expected = NullPointerException.class)
    public void loadFile_whenMultipartIsNull_throwsNullPointerException() throws IOException {
        fileService.loadFile(null);
    }

    @Test(expected = FileSystemException.class)
    public void loadFile_whenMultipartHaveNoExtension_throwsFileSystemException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "filename", "text/plain", "some xml".getBytes());
        fileService.loadFile(file);
    }

    @Test(expected = FileSystemException.class)
    public void loadFile_whenMultipartIsNotCsv_throwsFileSystemException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        fileService.loadFile(file);
    }

    @Test
    public void loadFile_whenMultipartIsCSV_returnFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "4:2,5,6,7;".getBytes());
        File fileReturn = fileService.loadFile(file);
        Assert.assertNotNull(fileReturn);
        Assert.assertTrue(fileReturn.getName().contains("filename"));
    }

    @Test(expected = FileEmptyException.class)
    public void loadFile_whenMultipartIsCSVAndEmpty_throwsFileEmptyException() throws IOException, DataFormatException, FileEmptyException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "".getBytes());
        File fileReturn = fileService.loadFile(file);
        fileService.readData(fileReturn);
    }

    @Test(expected = DataFormatException.class)
    public void readData_WhenContainsBadLines() throws IOException, DataFormatException, FileEmptyException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "4:2,5,6,7k;".getBytes());
        File fileReturn = fileService.loadFile(file);
        fileService.readData(fileReturn);
    }

    @Test
    public void readData_WhenFormatInLinesIsCorrect() throws IOException, DataFormatException, FileEmptyException {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "4:2,5,6,7;".getBytes());
        File fileReturn = fileService.loadFile(file);
        List<String> list  = fileService.readData(fileReturn);
        Assert.assertEquals("4:2,5,6,7;", list.get(0));
    }
}