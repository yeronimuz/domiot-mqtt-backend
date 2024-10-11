package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.SensorTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorTypeDtoMapper {
    default SensorTypeDto map(int sensorType) {
        for (SensorTypeDto type : SensorTypeDto.values()) {
            if (type.getId() == sensorType) {
                return type;
            }
        }
        return null;
    }

    default int map(SensorTypeDto sensorType) {
        return sensorType.getId();
    }
}
