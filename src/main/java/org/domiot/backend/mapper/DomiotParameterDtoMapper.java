package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.DomiotParameterDto;
import org.lankheet.domiot.entities.DomiotParameterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DomiotParameterDtoMapper {
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "value", target = "value")
//    @Mapping(source = "parameterType", target = "parameterType")

    DomiotParameterDto map(DomiotParameterEntity domiotParameterEntity);

    @Mapping(target = "id", ignore = true)
    DomiotParameterEntity map(DomiotParameterDto domiotParameterDto);
}
