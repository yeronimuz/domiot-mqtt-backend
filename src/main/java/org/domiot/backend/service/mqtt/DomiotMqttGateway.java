package org.domiot.backend.service.mqtt;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface DomiotMqttGateway {

    void sendToMqtt(String data);
}
