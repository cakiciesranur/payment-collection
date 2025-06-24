package com.eny.paymentcollection.exception;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = 5997680558667659568L;

    private int errorCode;
    private String message;

    public ErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
