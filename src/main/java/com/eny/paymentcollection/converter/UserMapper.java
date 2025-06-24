package com.eny.paymentcollection.converter;

import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.model.UserEntity;
import com.eny.paymentcollection.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<UpdateUserDto, UserEntity, UserResponse> {
    UserResponse toResource(UserEntity entity);

    UserEntity toEntity(UpdateUserDto dto);
}