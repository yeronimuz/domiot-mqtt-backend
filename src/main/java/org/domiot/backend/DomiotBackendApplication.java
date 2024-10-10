package org.domiot.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main SpringBoot application class
 */
@SpringBootApplication
@EnableJpaRepositories("org.lankheet.domiot.entities")
//@Import({SensorValueMapperImpl.class, SensorMapperImpl.class, MqttTopicPathMapperImpl.class, DomiotParameterMapperImpl.class})
@Slf4j
public class DomiotBackendApplication {
    /**
     * Entry point for the DomiotBackendApplication Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(DomiotBackendApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
