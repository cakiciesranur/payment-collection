package com.eny.paymentcollection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotAllowedOperationException extends RuntimeException {

    public NotAllowedOperationException(String message) {
        super(message);
    }

    public NotAllowedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}