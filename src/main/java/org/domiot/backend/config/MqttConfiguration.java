package org.domiot.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {

    @Value("${mqtt.url}")
    private String url;// tcp://localhost:1883
    @Value("${mqtt.userName}")
    private String userName; // johndoe
    @Value("${mqtt.password}")
    private String password; // secret
    @Value("${mqtt.caFilePath}")
    private String caFilePath; // /etc/pki/ca-trust/extracted/pem/tls-ca-bundle.pem
    @Value("${mqtt.crtFilePath}")
    private String crtFilePath; // null
    @Value("${mqtt.clientKeyFilePath}")
    private String clientKeyFilePath; // null
}
