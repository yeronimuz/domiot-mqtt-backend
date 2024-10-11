package org.domiot.backend.mapper;

import org.lankheet.domiot.domotics.dto.MqttTopicDto;
import org.lankheet.domiot.entities.MqttTopicEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MqttTopicDtoMapper {

    MqttTopicDto map(MqttTopicEntity mqttTopicEntity);

    MqttTopicEntity map(MqttTopicDto mqttTopicDto);
}
