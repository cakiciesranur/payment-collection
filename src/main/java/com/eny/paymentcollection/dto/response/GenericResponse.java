package com.eny.paymentcollection.dto.response;

import com.eny.paymentcollection.enums.ResponseType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenericResponse<T> {

    private ResponseType responseType;
    private int errorCode;
    private String message;
    private T data;

    public GenericResponse() {

    }

    public GenericResponse(ResponseType responseType, int errorCode, String message, T data) {
        this.responseType = responseType;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }
}
