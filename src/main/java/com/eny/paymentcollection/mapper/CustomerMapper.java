package com.eny.paymentcollection.mapper;

import com.eny.paymentcollection.dto.request.CustomerRequestDto;
import com.eny.paymentcollection.dto.response.CustomerResponseDto;
import com.eny.paymentcollection.model.CustomerEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper {

    CustomerEntity toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(CustomerEntity entity);

    List<CustomerResponseDto> toDtoList(List<CustomerEntity> entityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CustomerRequestDto dto, @MappingTarget CustomerEntity entity);
}
