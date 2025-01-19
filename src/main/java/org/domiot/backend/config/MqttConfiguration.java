package org.domiot.backend.config;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;

/**
 * The MQTT configuration using broker parameters from application.properties file
 */
@Configuration
@Slf4j
public class MqttConfiguration {

    @Value("${mqtt.url}")
    private String url;
    @Value("${mqtt.username}")
    private String userName;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.caFilePath}")
    private String caFilePath;
    @Value("${mqtt.crtFilePath}")
    private String crtFilePath;
    @Value("${mqtt.clientKeyFilePath}")
    private String clientKeyFilePath;
    @Value("${mqtt.clientId}")
    private String clientId;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(15000);
        options.setMaxReconnectDelay(5000);
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setServerURIs(new String[]{url});
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public IntegrationFlow mqttInFlow(MqttPahoClientFactory mqttClientFactory) {
        return IntegrationFlow.from(
                        new MqttPahoMessageDrivenChannelAdapter(this.clientId,
                                mqttClientFactory, "sensor/#", "register"))
                .route("headers['" + MqttHeaders.RECEIVED_TOPIC + "'].contains('sensor') ? 'sensorChannel' : 'registerChannel'")
                .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(UUID.randomUUID().toString(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("config");
        return messageHandler;
    }
}