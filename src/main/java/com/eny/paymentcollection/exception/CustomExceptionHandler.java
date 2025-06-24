package com.eny.paymentcollection.exception;

import com.eny.paymentcollection.constants.ErrorMessageConstant;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.service.GenericResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @Autowired
    private GenericResponseService genericResponseService;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponse> handleBadRequest(BadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GenericResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        log.error("Email already exists: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(
                ErrorMessageConstant.USERNAME_EXIST);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<GenericResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        log.error("Username already exists: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(
                ErrorMessageConstant.USERNAME_EXIST);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<GenericResponse> handleUserCreation(UserCreationException ex) {
        log.error("User creation failed: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(
                ErrorMessageConstant.PROCESS_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GenericResponse> handleDuplicateResource(DuplicateResourceException ex) {
        log.error("Duplicate resource: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAllowedOperationException.class)
    public ResponseEntity<GenericResponse> handleNotAllowedOperation(NotAllowedOperationException ex) {
        log.error("Not allowed operation: {}", ex.getMessage());
        GenericResponse response = genericResponseService.createResponseWithError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        GenericResponse response = genericResponseService.createResponseWithError(
                "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        GenericResponse response = genericResponseService.createResponseWithError(
                ErrorMessageConstant.UNKNOWN_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}