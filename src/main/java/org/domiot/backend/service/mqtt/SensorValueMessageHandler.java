package org.domiot.backend.service.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorValueMessageHandler implements MessageHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorValueService sensorValueService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.trace("Received Message: {}", message);

        SensorValueDto sensorValue = null;
        try {
            sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValueDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
    }
}
