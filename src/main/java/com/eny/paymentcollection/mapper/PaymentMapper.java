package com.eny.paymentcollection.mapper;

import com.eny.paymentcollection.dto.request.PaymentDto;
import com.eny.paymentcollection.model.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentMapper {

    // Entity -> DTO (Flatten Customer Info)
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerCode", target = "customerCode")
    @Mapping(source = "customer.companyName", target = "customerCompanyName")
    PaymentDto toDto(PaymentEntity entity);

    List<PaymentDto> toDtoList(List<PaymentEntity> entities);

    // DTO -> Entity (you handle setting customer manually in service)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // to be set manually
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    PaymentEntity toEntity(PaymentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(PaymentDto dto, @MappingTarget PaymentEntity entity);
}
