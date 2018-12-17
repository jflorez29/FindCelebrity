package com.globant.vividseats.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(DataFormatException.class)
    public @ResponseBody
    HttpEntity handleDataFormatException(HttpServletRequest request, DataFormatException e) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public @ResponseBody
    HttpEntity handleIOException(HttpServletRequest request, IOException e) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    HttpEntity handleException(HttpServletRequest request, Exception e) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
