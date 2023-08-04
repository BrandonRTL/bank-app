package com.loanservice.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CreditDeniedException extends RuntimeException {
    private Map<String, String> errors;

}
