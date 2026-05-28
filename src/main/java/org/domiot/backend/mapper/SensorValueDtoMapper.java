package org.domiot.backend.mapper;

import org.domiot.dto.SensorValueDto;
import org.domiot.entities.SensorValueEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {SensorDtoMapper.class})
public interface SensorValueDtoMapper {

    SensorValueDto map(SensorValueEntity sensorValue);

    SensorValueEntity map(SensorValueDto sensorValueDto);
}
