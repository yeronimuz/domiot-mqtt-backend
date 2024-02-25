package org.domiot.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * General MQTT message handler
     *
     * @return Functional interface containing handling lambda's for handling Device and SensorValue messages.
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            SensorValueDto sensorValue = null;

            try {
                sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValueDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            sensorValueService.saveSensorValue(sensorValue);
            log.info("SensorValue: {}", sensorValue);
        };
    }
}
