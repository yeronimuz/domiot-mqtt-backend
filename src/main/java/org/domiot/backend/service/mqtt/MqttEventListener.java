package org.domiot.backend.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttIntegrationEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttEventListener implements ApplicationListener<MqttIntegrationEvent> {
    @Override
    public void onApplicationEvent(MqttIntegrationEvent event) {
        if (event instanceof MqttConnectionFailedEvent) {
            log.error("mqtt connection failed: {}", event);
        }
        log.info("MqttIntegrationEvent: {}", event);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
