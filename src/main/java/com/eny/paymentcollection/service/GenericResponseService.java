package com.eny.paymentcollection.service;

import com.eny.paymentcollection.constants.ErrorMessageConstant;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.enums.ResponseType;
import com.eny.paymentcollection.model.ErrorMessageEntity;
import com.eny.paymentcollection.service.error.ErrorMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Utility service to create standardized API responses.
 */
@Service
@RequiredArgsConstructor
public class GenericResponseService {
    private final ErrorMessageService errorMessageService;

    //TODO: provide these methods by util classes.
    public <T> GenericResponse<T> createSuccessResponse(String message, T data) {
        return new GenericResponse<T>()
                .setResponseType(ResponseType.SUCCESS)
                .setMessage(message)
                .setData(data);
    }

    public <T> GenericResponse<T> createSuccessResponse(T data) {
        return createSuccessResponse(null, data);
    }

    public <T> GenericResponse<T> createErrorResponse(String message) {
        return createErrorResponse(ErrorMessageConstant.UNKNOWN_ERROR, message, null);
    }

    public <T> GenericResponse<T> createErrorResponse(String message, T data) {
        return createErrorResponse(ErrorMessageConstant.UNKNOWN_ERROR, message, data);
    }

    public <T> GenericResponse<T> createErrorResponse(int errorCode) {
        ErrorMessageEntity errorMessage = errorMessageService.getErrorMessage(errorCode);
        return createErrorResponse(errorCode, errorMessage.getMessage(), null);
    }

    public <T> GenericResponse<T> createErrorResponse(int errorCode, String message, T data) {
        return new GenericResponse<T>()
                .setResponseType(ResponseType.ERROR)
                .setErrorCode(errorCode)
                .setMessage(message)
                .setData(data);
    }

    public <T> GenericResponse<T> createRedirectResponse(String redirectUrl, T data) {
        return new GenericResponse<T>()
                .setResponseType(ResponseType.REDIRECT)
                .setMessage(redirectUrl)
                .setData(data);
    }

    public <T> GenericResponse<T> createRedirectResponse(String redirectUrl) {
        return createRedirectResponse(redirectUrl, null);
    }
}