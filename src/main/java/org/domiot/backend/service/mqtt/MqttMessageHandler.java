package org.domiot.backend.service.mqtt;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.service.DeviceService;
import org.domiot.backend.service.SensorValueService;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttMessageHandler {
    private ObjectMapper objectMapper;
    private SensorValueService sensorValueService;
    private DeviceService deviceService;

    public MqttMessageHandler(
            ObjectMapper objectMapper,
            SensorValueService sensorValueService,
            DeviceService deviceService) {
        this.objectMapper = objectMapper;
        this.sensorValueService = sensorValueService;
        this.deviceService = deviceService;
    }

    @Bean
    public IntegrationFlow mqttInFlow(MqttPahoClientFactory mqttClientFactory) {
        String clientId = UUID.randomUUID().toString();
        return IntegrationFlow.from(
                        new MqttPahoMessageDrivenChannelAdapter(clientId,
                                mqttClientFactory, "sensor/*", "register"))
                .route("headers['" + MqttHeaders.RECEIVED_TOPIC + "']")
                .get();
    }

    @Bean
    public IntegrationFlow flowSensorValues() {
        return IntegrationFlow.from("sensor/*")
                .handle((payload, headers) -> {
                    log.info("message from sensor {} : {}", payload, headers);
                    handleSensorValue(payload);
                    // TODO: Handle saving values
                    return null;
                })
                .get();
    }

    @Bean
    public IntegrationFlow flowRegister() {
        return IntegrationFlow.from("register")
                .handle((payload, headers) -> {
                    log.info("message from register {}: {}", payload, headers);
                    handleRegistration(payload);
                    return null;
                })
                .get();
    }

//     @Override
//    public void handleMessage(Message<?> message) throws MessagingException {
//        log.trace("Received Message: {}", message);
//
//        if (Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString().contains("sensor")) {
//            handleSensorValue(message);
//            return;
//        }
//        if (Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString().contains("register")) {
//            handleRegistration(message);
//            return;
//        }
//        log.info("Ignoring Received Message: {}", message);
//    }

    private void handleRegistration(Object payload) {
        DeviceDto deviceDto;
        try {
            deviceDto = objectMapper.readValue(payload.toString(), DeviceDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DeviceDto returnDeviceDto = deviceService.saveDevice(deviceDto);
        // TODO: Create event for frontend
        // TODO: Activate return path
        log.info("Device registered: {}", returnDeviceDto);
    }

    private void handleSensorValue(Object payload) {
        SensorValueDto sensorValue;
        try {
            sensorValue = objectMapper.readValue(payload.toString(), SensorValueDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SensorValueDto sensorValueDtoSaved = sensorValueService.saveSensorValue(sensorValue);
    }
}
