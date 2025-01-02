package org.domiot.backend.service.mqtt;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.event.DeviceConfigEvent;
import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttMessageHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SensorValueService sensorValueService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public MqttMessageHandler(SensorValueService sensorValueService, DeviceService deviceService, ApplicationEventPublisher eventPublisher) {
        this.sensorValueService = sensorValueService;
        this.deviceService = deviceService;
        this.eventPublisher = eventPublisher;

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public IntegrationFlow flowSensorValues() {
        return IntegrationFlow.from("sensorChannel")
                .handle((payload, headers) -> {
                    handleSensorValue(payload);
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow flowRegister() {
        return IntegrationFlow.from("registerChannel")
                .handle((payload, headers) -> {
                    handleRegistration(payload);
                    return null;
                })
                .get();
    }

    private void handleRegistration(Object payload) {
        DeviceDto deviceDto;
        log.info("Registering: {}", payload);
        try {
            deviceDto = objectMapper.readValue(payload.toString(), DeviceDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DeviceDto returnDeviceDto = deviceService.saveDevice(deviceDto);
        log.info("Publish device registered: {}", returnDeviceDto);
        DeviceConfigEvent deviceConfigEvent = new DeviceConfigEvent(this, returnDeviceDto);
        eventPublisher.publishEvent(deviceConfigEvent);
    }

    private void handleSensorValue(Object payload) {
        SensorValueDto sensorValue;
        log.debug("New sensor value {}", payload);
        try {
            sensorValue = objectMapper.readValue(payload.toString(), SensorValueDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
    }
}
