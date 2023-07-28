package com.loanservice.conveyor.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
}
