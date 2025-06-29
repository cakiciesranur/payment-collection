package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;

import java.util.List;

public interface IUserService {
    UserResponseDto createUser(SignUpDto signUpDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(UpdateUserDto dto);

    UserResponseDto getByUsername(String username);
}
