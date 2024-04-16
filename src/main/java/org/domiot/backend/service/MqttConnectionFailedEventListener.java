package org.domiot.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class MqttConnectionFailedEventListener {
    @Bean
    public ApplicationListener<?> eventListener() {
        return (ApplicationListener<MqttConnectionFailedEvent>) event -> {
            log.error("MQTT connection failed");
            log.error(event.getCause().getMessage());
            log.error(Arrays.toString(event.getCause().getStackTrace()));
        };
    }
}
