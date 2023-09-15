package com.loanservice.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
@AllArgsConstructor
@Slf4j
public class ControllerAdvice {

    private final ObjectMapper objectMapper;

    /**
     * If exceptions occur during the operation of the feign client
     * (for example, a loan is denied), a message will be sent to the service
     * @param e caught exception
     * @return Entity with reasons for unexpected behavior
     */
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

}
