package com.eny.paymentcollection.mapper;

import com.eny.paymentcollection.dto.request.CustomerDto;
import com.eny.paymentcollection.model.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper {

    CustomerDto toDto(CustomerEntity entity);

    List<CustomerDto> toDtoList(List<CustomerEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    CustomerEntity toEntity(CustomerDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(CustomerDto dto, @MappingTarget CustomerEntity entity);
}