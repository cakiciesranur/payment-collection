package com.eny.paymentcollection.service;

import com.eny.paymentcollection.constants.ErrorMessageConstant;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.enums.ResponseType;
import com.eny.paymentcollection.model.ErrorMessageEntity;
import com.eny.paymentcollection.service.error.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericResponseService {

    @Autowired
    private ErrorMessageService errorMessageService;

    public <T> GenericResponse createResponse(ResponseType responseType, String message, T data, int errorCode) {
        GenericResponse<T> response = new GenericResponse<T>()
                .setData(data)
                .setResponseType(responseType)
                .setMessage(message)
                .setErrorCode(errorCode);
        return response;
    }

    public <T> GenericResponse createResponseNoError(String message, T data) {
        return createResponse(ResponseType.SUCCESS, message, data, ErrorMessageConstant.NO_ERROR);
    }

    public <T> GenericResponse createResponseNoError(T data) {
        return createResponse(ResponseType.SUCCESS, null, data, ErrorMessageConstant.NO_ERROR);
    }

    public <T> GenericResponse createResponseWithError(String message) {
        return createResponse(ResponseType.ERROR, message, null, ErrorMessageConstant.UNKNOWN_ERROR);
    }

    public <T> GenericResponse createResponseWithError(String message, T data) {
        return createResponse(ResponseType.ERROR, message, data, ErrorMessageConstant.UNKNOWN_ERROR);
    }

    public <T> GenericResponse createResponseWithError(int errorCode, T data) {
        ErrorMessageEntity errorMessage = errorMessageService.getErrorMessage(errorCode);
        return createResponse(ResponseType.ERROR, errorMessage.getMessage(), data, errorCode);
    }

    public <T> GenericResponse createResponseWithError(int errorCode) {
        ErrorMessageEntity errorMessage = errorMessageService.getErrorMessage(errorCode);
        return createResponse(ResponseType.ERROR, errorMessage.getMessage(), null, errorCode);
    }

    public <T> GenericResponse createResponseWithRedirect(String newURl, T data) {
        return createResponse(ResponseType.REDIRECT, newURl, data, ErrorMessageConstant.NO_ERROR);
    }

    public <T> GenericResponse createResponseWithRedirect(String relativeNewURL) {
        return createResponse(ResponseType.REDIRECT, relativeNewURL, null, ErrorMessageConstant.NO_ERROR);
    }
}