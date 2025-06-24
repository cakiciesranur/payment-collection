package com.eny.paymentcollection.converter;

public interface Converter<DTO, Entity, Resource> {

    Resource toResource(Entity entity);

    Entity toEntity(DTO dto);
}
