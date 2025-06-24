package com.eny.paymentcollection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionMessage> BadRequestException(BadRequestException ex) {
        ExceptionMessage error = new ExceptionMessage("Bad Request", ex.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public final ResponseEntity<ExceptionMessage> EmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ExceptionMessage error = new ExceptionMessage("Username or Email Address already in use!", ex.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public final ResponseEntity<ExceptionMessage> UsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ExceptionMessage error = new ExceptionMessage("Username or Email Address already in use!", ex.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserCreationException.class)
    public final ResponseEntity<ExceptionMessage> UserCreationException(UserCreationException ex) {
        ExceptionMessage error = new ExceptionMessage("Something went wrong while creating user!", ex.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFound.class)
    public final ResponseEntity<ExceptionMessage> UserNotFound(UserNotFound ex) {
        ExceptionMessage error = new ExceptionMessage("User can not find!", ex.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}