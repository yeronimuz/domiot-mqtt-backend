package org.domiot.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.service.mqtt.MqttMessageHandler;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

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
    @Value("mqtt.clientId")
    private String clientId;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(15000);
        options.setMaxReconnectDelay(5000);
        options.setAutomaticReconnect(true);
        options.setServerURIs(new String[]{url});
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    // TODO: Check https://stackoverflow.com/questions/67391175/how-can-we-have-different-handlers-to-subscribe-messages-from-different-topics-u

    @Bean
    public IntegrationFlow mqttInbound(MqttPahoClientFactory mqttClientFactory,
                                       MqttMessageHandler mqttMessageHandler) {
        // randomize the clientId, thus preventing reconnection problems
        clientId += System.nanoTime();
        MqttPahoMessageDrivenChannelAdapter channelAdapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory, "#");
        channelAdapter.setErrorChannelName("errorChannel");
        channelAdapter.setQos(1);
        return IntegrationFlow.from(channelAdapter)
                .handle(mqttMessageHandler)
                .get();
    }
}
