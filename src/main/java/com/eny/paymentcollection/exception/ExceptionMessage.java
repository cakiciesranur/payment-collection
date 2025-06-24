package com.eny.paymentcollection.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionMessage {
    String message;
    String details;
    Date date;

    public ExceptionMessage(String message, String details, Date date) {
        this.message = message;
        this.details = details;
        this.date = date;
    }
}
