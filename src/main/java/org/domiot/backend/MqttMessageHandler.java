package org.domiot.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.model.SensorValue;
import org.lankheet.domiot.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttMessageHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorValueService sensorValueService;
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            SensorValue sensorValue;
            try {
                sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValue.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("SensorValue: {}", sensorValue);
            sensorValueService.saveSensorValue(sensorValue);
        };
    }
}
