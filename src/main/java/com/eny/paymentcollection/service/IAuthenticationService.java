package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.LoginRequestDto;
import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.response.LoginResponseDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;

public interface IAuthenticationService {

    UserResponseDto register(SignUpDto request);

    LoginResponseDto login(LoginRequestDto loginRequest);

    UserResponseDto getCurrentUser();
}
