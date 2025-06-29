package com.eny.paymentcollection.mapper;

import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;
import com.eny.paymentcollection.enums.RoleName;
import com.eny.paymentcollection.model.RoleEntity;
import com.eny.paymentcollection.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    // RoleEntity -> RoleName mapping
    @Named("mapRoles")
    default Set<RoleName> mapRoles(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserResponseDto toResponse(UserEntity entity);

    UserEntity toEntity(UpdateUserDto dto);

    void updateEntityFromDto(UpdateUserDto dto, @MappingTarget UserEntity entity);

    List<UserResponseDto> toResponseList(List<UserEntity> entities);
}
