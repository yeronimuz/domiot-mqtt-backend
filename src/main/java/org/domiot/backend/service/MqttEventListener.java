package org.domiot.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.core.MqttPahoComponent;
import org.springframework.integration.mqtt.event.MqttIntegrationEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttEventListener implements ApplicationListener<MqttIntegrationEvent> {
    @Override
    public void onApplicationEvent(MqttIntegrationEvent event) {
        MqttPahoComponent source = event.getSourceAsType();
        String beanName = source.getBeanName();
        MqttConnectOptions options = source.getConnectionInfo();
        log.info("Received MqttIntegrationEvent: {}", beanName);
        log.info("Connection info: {}", options);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
