package org.domiot.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.lankheet.domiot.domotics.dto.DomiotParameterDto;
import org.lankheet.domiot.domotics.dto.MqttTopicDto;
import org.lankheet.domiot.domotics.dto.SensorDto;
import org.lankheet.domiot.domotics.dto.SensorTypeDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.SensorType;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class SensorValueDtoMapperTest {

    @Autowired
    private SensorValueDtoMapper sensorValueDtoMapper;

    @Test
    void testMap() {
        SensorValueDto sensorValueDto = SensorValueDto.builder()
                .sensor(SensorDto.builder()
                        .sensorType(SensorTypeDto.GAS_SENSOR)
                        .deviceMac("AA:BB:CC:DD:EE:FF")
                        .mqttTopic(new MqttTopicDto("gas", "/path/to/topic"))
                        .build()).build();
        sensorValueDto.getSensor().addParameter(new DomiotParameterDto("parameter", "number", 1.55, true));

        SensorValueEntity sensorValueEntity = sensorValueDtoMapper.map(sensorValueDto);

        assertEquals(SensorType.GAS_SENSOR, sensorValueEntity.getSensorEntity().getSensorType());
        assertEquals("AA:BB:CC:DD:EE:FF", sensorValueEntity.getSensorEntity().getDeviceEntity().getMacAddress());
        assertEquals(1, sensorValueEntity.getSensorEntity().getParameterEntities().size());
    }
}
