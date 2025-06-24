package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.response.GenericResponse;

public interface IAuthenticationService {

    GenericResponse login(String usernameOrEmail, String password);
}
