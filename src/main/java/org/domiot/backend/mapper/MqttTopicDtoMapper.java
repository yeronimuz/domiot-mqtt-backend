package org.domiot.backend.mapper;

import org.domiot.dto.MqttTopicDto;
import org.domiot.entities.MqttTopicEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MqttTopicDtoMapper {

    MqttTopicDto map(MqttTopicEntity mqttTopicEntity);

    MqttTopicEntity map(MqttTopicDto mqttTopicDto);
}
