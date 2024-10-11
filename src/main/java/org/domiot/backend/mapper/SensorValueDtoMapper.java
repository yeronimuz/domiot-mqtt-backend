package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {SensorDtoMapper.class})
public interface SensorValueDtoMapper {

    @Mapping(source = "sensorEntity", target = "sensor")
    SensorValueDto map(SensorValueEntity sensorValue);


    @Mapping(source = "sensor", target = "sensorEntity")
    SensorValueEntity map(SensorValueDto sensorValueDto);
}
