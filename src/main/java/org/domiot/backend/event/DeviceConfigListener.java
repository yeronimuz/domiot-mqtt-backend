package org.domiot.backend.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.service.mqtt.DomiotMqttGateway;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
/**
 * DeviceConfigEvent objects will be sent by the service that updates device configuration.
 * This event listener responds to the events emitted.
 */
public class DeviceConfigListener implements ApplicationListener<DeviceConfigEvent> {
    @Autowired
    private ApplicationContext applicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onApplicationEvent(DeviceConfigEvent event) {
        log.trace("onApplicationEvent {}", event);
        DeviceDto device = event.getDevice();

        DomiotMqttGateway gateway = applicationContext.getBean(DomiotMqttGateway.class);
        try {
            log.info("Sending device config event");
            gateway.sendToMqtt(objectMapper.writeValueAsString(device));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
