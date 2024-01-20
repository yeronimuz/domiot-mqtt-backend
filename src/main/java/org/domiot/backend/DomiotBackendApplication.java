package org.domiot.backend;

import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.mapper.DomiotParameterMapperImpl;
import org.lankheet.domiot.mapper.MqttTopicPathMapperImpl;
import org.lankheet.domiot.mapper.SensorMapperImpl;
import org.lankheet.domiot.mapper.SensorValueMapperImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.lankheet.domiot.entities")
@Import({SensorValueMapperImpl.class, SensorMapperImpl.class, MqttTopicPathMapperImpl.class, DomiotParameterMapperImpl.class})
@Slf4j
public class DomiotBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(DomiotBackendApplication.class, args);
    }

}
