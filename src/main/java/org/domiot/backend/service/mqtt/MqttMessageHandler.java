package org.domiot.backend.service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.mapper.DeviceDtoMapper;
import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
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
    DeviceDtoMapper deviceMapper;

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
        DeviceDto deviceDto;
        try {
            deviceDto = objectMapper.readValue(message.getPayload().toString(), DeviceDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DeviceDto returnDeviceDto = deviceService.saveDevice(deviceDto);
        // TODO: Create event for frontend
        // TODO: Activate return path
        log.info("Device registered: {}", returnDeviceDto);
    }

    private void handleSensorValue(Message<?> message) {
        SensorValueDto sensorValue;
        try {
            sensorValue = objectMapper.readValue(message.getPayload().toString(), SensorValueDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
    }
}
