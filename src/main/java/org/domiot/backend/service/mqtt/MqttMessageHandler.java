package org.domiot.backend.service.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttMessageHandler implements MessageHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorValueService sensorValueService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    DeviceMapper deviceMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.trace("Received Message: {}", message);

        if (Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString().contains("sensor")) {
            handleSensorValue(message);
            return;
        }
        if (Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString().contains("register")) {
            handleRegistration(message);
            return;
        }
        log.info("Ignoring Received Message: {}", message);
    }

    private void handleRegistration(Message<?> message) {
        DeviceDto deviceDto = objectMapper.convertValue(message.getPayload(), DeviceDto.class);
        DeviceDto returnDeviceDto = deviceService.saveDevice(deviceDto);
        // TODO: Create event for frontend
        // TODO: Activate return path
    }

    private void handleSensorValue(Message<?> message) {
        SensorValueDto sensorValue = null;
        try {
            sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValueDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
    }
}
