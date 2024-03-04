package org.domiot.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorValueMessageHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorValueService sensorValueService;
    @Autowired
    private DeviceService deviceService;

    private Integer lastMqttId = 0;

    /**
     * General MQTT message handler
     *
     * @return Functional interface containing handling lambda's for handling Device and SensorValue messages.
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            Object mqttId = message.getHeaders().entrySet().stream()
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue))
                    .get("mqtt_id");
            if ((mqttId instanceof Integer) && mqttId.equals(lastMqttId)) {
                log.trace("Dropping mqtt id {}", mqttId);
            } else {
                SensorValueDto sensorValue = null;
                log.trace("Msg: {}", message);
                try {
                    sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValueDto.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
                this.lastMqttId = (Integer) mqttId;
            }
        };
    }
}
