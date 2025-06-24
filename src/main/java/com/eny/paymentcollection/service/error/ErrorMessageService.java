package com.eny.paymentcollection.service.error;


import com.eny.paymentcollection.constants.ErrorMessageConstant;
import com.eny.paymentcollection.model.ErrorMessageEntity;
import com.eny.paymentcollection.repository.ErrorMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by kmluns
 **/
@Service
public class ErrorMessageService {

    @Autowired
    private ErrorMessageRepository errorMessageRepository;

    public ErrorMessageEntity getErrorMessage(int errorCode) {
        ErrorMessageEntity errorMessage = errorMessageRepository.findByErrorCode(errorCode);
        if (errorMessage == null) {
            return errorMessageRepository.findByErrorCode(ErrorMessageConstant.UNKNOWN_ERROR);
        }
        return errorMessage;
    }

    public String getErrorMessageText(int errorCode) {
        ErrorMessageEntity errorMessage = errorMessageRepository.findByErrorCode(errorCode);
        if (errorMessage == null) {
            return errorMessageRepository.findByErrorCode(ErrorMessageConstant.UNKNOWN_ERROR).getMessage();
        }
        return errorMessage.getMessage();
    }
}
