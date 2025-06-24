package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.model.UserEntity;

import java.util.List;

public interface IUserService {
    UserEntity createUser(SignUpDto signUpDto);

    List<UserEntity> getAllUsers();

    UserEntity updateUser(UpdateUserDto dto);
}
