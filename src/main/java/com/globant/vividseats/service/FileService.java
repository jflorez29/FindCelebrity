package com.globant.vividseats.service;

import com.globant.vividseats.exception.DataFormatException;
import com.globant.vividseats.exception.FileEmptyException;
import com.globant.vividseats.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to manage all matters of files.
 */
@Service
public class FileService {

    private final String uploadPath;

    public FileService(@Value("${file.upload-dir}") String uploadPath){
        this.uploadPath = uploadPath;
    }

    /**
     * Create file from MultipartFile sent from request, after validate if it is a correct file to process.
     * @param multipartFile File from request
     * @return Local file create from multipartFile
     * @throws IOException
     */
    public File loadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null){
            String extension = getFileExtension(multipartFile);
            if (extension.equalsIgnoreCase(Constants.CSV_FILE)){
                Path path = Paths.get(uploadPath + multipartFile.getOriginalFilename());
                byte[] bytes = multipartFile.getBytes();
                Files.write(path, bytes);
                return path.toFile();
            }else{
                throw new FileSystemException(String.format("Extension %s is not valid, use CSV file", extension));
            }
        }else {
            throw new NullPointerException("File cannot be null");
        }
    }

    /**
     * Read data from local file, validate if data is correct to processed and finally add them to List.
     * @param file local file created from multipart
     * @return List of String with data to process
     * @throws IOException
     * @throws DataFormatException When line of file not matches with prescribed format
     */
    public List<String> readData(File file) throws IOException, DataFormatException, FileEmptyException {
        List<String> data = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String st;
        while ((st = bufferedReader.readLine()) != null) {
            if (!st.isEmpty()){
                if (!validateStructure(st)){
                    throw new DataFormatException(st);
                }
                data.add(st);
            }
        }
        if (data.isEmpty()) throw new FileEmptyException(file.getName());
        return data;
    }

    /**
     * Validate if line matches with prescribed format
     * @param line line to validate
     * @return true if matches otherwise false
     */
    private Boolean validateStructure(String line){
        return line.matches(Constants.REGEX_VALIDATE_DATA);
    }

    /**
     * Return extension of file.
     * @param multipartFile file from request
     * @return extension of file
     */
    private String getFileExtension(MultipartFile multipartFile) throws FileSystemException {
        if (multipartFile.getOriginalFilename().contains(".")){
            String fileExtension = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf('.'));
            return fileExtension;
        }else{
            throw new FileSystemException("File must have an extension .CSV");
        }
    }
}
