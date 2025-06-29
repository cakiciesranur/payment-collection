package com.eny.paymentcollection.mapper;

import com.eny.paymentcollection.dto.request.PaymentRequestDto;
import com.eny.paymentcollection.dto.response.PaymentResponseDto;
import com.eny.paymentcollection.model.PaymentEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerCode", target = "customerCode")
    @Mapping(source = "customer.companyName", target = "customerCompanyName")
    PaymentResponseDto toResponseDto(PaymentEntity entity);

    List<PaymentResponseDto> toResponseDtoList(List<PaymentEntity> entities);

    PaymentEntity toEntity(PaymentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PaymentRequestDto dto, @MappingTarget PaymentEntity entity);
}
