package com.loanservice.conveyor.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.debug("Handle MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();
        for (var i : e.getBindingResult().getFieldErrors()) {
            errors.put(i.getField(), i.getDefaultMessage());
        }
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Input data is not valid");
        response.setErrors(errors);
        log.debug(response.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * If the loan was denied, the service will return a message about it,
     * as well as a list of reasons for denying the loan
     * @param e contains information about what exactly caused the denial
     * @return loan denial message
     */
    @ExceptionHandler(CreditDeniedException.class)
    public ResponseEntity<ErrorResponse> handleCreditDeniedException(CreditDeniedException e) {
        log.debug("Handle CreditDeniedException");
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Credit denied");
        response.setErrors(e.getErrors());
        log.debug(response.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
