package com.eny.paymentcollection.service.error;


import com.eny.paymentcollection.constants.ErrorMessageConstant;
import com.eny.paymentcollection.model.ErrorMessageEntity;
import com.eny.paymentcollection.repository.ErrorMessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class ErrorMessageDataService {
    private final ErrorMessageRepository errorMessageRepository;

    //TODO: This service should run one time! Then please comment " @PostConstruct ".
    @PostConstruct
    public void createErrorMessages() {

        errorMessageRepository.deleteAll();

        if (errorMessageRepository.count() == 0) {
            LinkedList<ErrorMessageEntity> errorMessages = new LinkedList<>();

            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.AUTHENTICATION_ERROR, "Username or password wrong!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.PARSE_ERROR, "Something wrong in your information!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.PROCESS_ERROR, "Something happened while process. Please try again"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.JWT_EXPIRED_ERROR, "Your session has been expired, please login again."));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.BAD_CREDENTIALS_ERROR, "You have to login!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.USERNAME_EXIST, "The username already using!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.NOT_FOUND, "Nothing found, something wrong!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.AUTHORIZATION_ERROR, "You are not authorized!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.UNKNOWN_ERROR, "Unknown Error!"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.CUSTOMER_NOT_FOUND, "Customer not found"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.PAYMENT_NOT_FOUND, "Payment not found"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.DUPLICATE_CUSTOMER_CODE, "Customer code already exists"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.INVALID_PAYMENT_AMOUNT, "Invalid payment amount"));
            errorMessages.add(new ErrorMessageEntity(ErrorMessageConstant.PAYMENT_ALREADY_COMPLETED, "Payment already completed"));

            errorMessageRepository.saveAll(errorMessages);
        }
    }

}