package com.esa.main.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApiRestException.class})
    public ResponseEntity<Object> handleApiException(ApiRestException e) {
        HttpStatus badRequest = BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    ResponseEntity<Object> exceptionHandler(ValidationException ex) {
        JSONObject response = new JSONObject();
        response.put("message", ex.getMessage().replace("registerCitizenESA.regularAmount: ", ""));
        response.put("timestamp", ZonedDateTime.now(ZoneId.of("Z")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", BAD_REQUEST);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> constraintExceptionHandler(ConstraintViolationException ex) {
        JSONObject response = new JSONObject();
        response.put("message", ex.getMessage().replace("registerCitizenESA.regularAmount: ", ""));
        response.put("timestamp", ZonedDateTime.now(ZoneId.of("Z")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", BAD_REQUEST);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }



}
