package com.globant.vividseats.exception;

/**
 * Exception to handle errors about format of lines in file
 */
public class DataFormatException extends Exception {

    public DataFormatException(String line){
        super("Line not matches with format : " + line);
    }

    public DataFormatException(Exception e){
        super(e);
    }
}
