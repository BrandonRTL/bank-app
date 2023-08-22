package com.loanservice.application.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
@AllArgsConstructor
@Slf4j
public class ControllerAdvice {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(FeignException.FeignClientException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        log.error("Caught feignexception: {}", e.contentUTF8());
        try {
            ErrorResponse response = objectMapper.readValue(e.contentUTF8(), ErrorResponse.class);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException ex) {
            log.error("Failed to map errors");
            return new ResponseEntity<>(new ErrorResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Caught methodargumentnotvalidexception");
        Map<String, String> errors = new HashMap<>();
        for (var i : e.getBindingResult().getFieldErrors()) {
            errors.put(i.getField(), i.getDefaultMessage());
        }
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Input data is not valid");
        response.setErrors(errors);
        log.error(response.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
