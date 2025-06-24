package com.eny.paymentcollection.repository;

import com.eny.paymentcollection.model.ErrorMessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ErrorMessageRepository extends CrudRepository<ErrorMessageEntity, String> {

    ErrorMessageEntity findByErrorCode(int errorCode);
}
