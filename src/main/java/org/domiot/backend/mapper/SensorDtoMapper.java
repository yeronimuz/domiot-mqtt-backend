package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.SensorDto;
import org.lankheet.domiot.entities.SensorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SensorTypeDtoMapper.class, MqttTopicDtoMapper.class, DomiotParameterDtoMapper.class})
public interface SensorDtoMapper {

    @Mapping(source = "mqttTopic", target = "mqttTopic.path")
    @Mapping(source = "parameterEntities", target = "parameters")
    @Mapping(target = "deviceMac", source = "deviceEntity.macAddress")
    SensorDto map(SensorEntity sensorEntity);

    @Mapping(source = "deviceMac", target = "deviceEntity.macAddress")
    @Mapping(source = "parameters", target = "parameterEntities")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "mqttTopic.path", target = "mqttTopic")
    SensorEntity map(SensorDto sensorDto);
}
