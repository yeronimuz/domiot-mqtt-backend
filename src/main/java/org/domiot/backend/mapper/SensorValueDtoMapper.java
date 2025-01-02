package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {SensorDtoMapper.class})
public interface SensorValueDtoMapper {

    SensorValueDto map(SensorValueEntity sensorValue);

    SensorValueEntity map(SensorValueDto sensorValueDto);
}
