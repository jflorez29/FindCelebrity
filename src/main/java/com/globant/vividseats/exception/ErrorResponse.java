package com.globant.vividseats.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private int code;
    private String message;

}
