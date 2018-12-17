package com.globant.vividseats.exception;

/**
 * Exception when file not contains data
 */
public class FileEmptyException extends Exception {

    public FileEmptyException(Exception e){
        super(e);
    }

    public FileEmptyException(String filename){
        super("File is empty : " + filename);
    }
}
