package org.domiot.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.domotics.datatypes.SensorValue;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttMessageHandler {
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            SensorValue sensorValue;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValue.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println(sensorValue);
            
        };
    }
}
