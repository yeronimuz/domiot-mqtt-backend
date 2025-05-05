package org.domiot.backend.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.database.SensorEntityRepository;
import org.domiot.backend.database.SensorValueEntityRepository;
import org.eclipse.paho.client.mqttv3.*;
        import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorDto;
import org.lankheet.domiot.domotics.dto.SensorTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.MountableFile;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        "logging.config=classpath:logback-spring.xml"
})
public abstract class DomiotBackendTestBase {
    protected static final int MQTT_PORT = 1883;
    protected static final String CONFIG_TOPIC = "config";
    protected static final String REGISTER_TOPIC = "register";
    protected static final String SENSOR_VALUE_TOPIC = "sensor/meterbox/ct2";

    protected static final BlockingQueue<DeviceDto> receivedMessages = new LinkedBlockingQueue<>();

    protected static final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    protected static MqttClient mqttClient;

    protected final String macAddress = UUID.randomUUID().toString();

    @Autowired
    protected MqttPahoClientFactory mqttClientFactory;

    @Autowired
    protected DeviceEntityRepository deviceRepository;

    @Autowired
    protected SensorEntityRepository sensorRepository;

    @Autowired
    protected SensorValueEntityRepository sensorValueRepository;

    protected static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:10.6")
            .withDatabaseName("domiot")
            .withUsername("domiot")
            .withPassword("domiot");

    protected static final GenericContainer<?> mosquitto = new GenericContainer<>("eclipse-mosquitto:2.0")
            .withExposedPorts(MQTT_PORT)
            .withCopyFileToContainer(MountableFile.forClasspathResource("mosquitto/mosquitto.conf"),
                    "/mosquitto/config/mosquitto.conf")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mosquitto/passwords.txt"),
                    "/mosquitto/config/passwords.txt")
            .withStartupTimeout(Duration.ofSeconds(15));

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);

        registry.add("mqtt.url", () -> "tcp://localhost:" + mosquitto.getMappedPort(MQTT_PORT));
        registry.add("mqtt.clientId", () -> "test-client");
        registry.add("mqtt.username", () -> "johndoe");
        registry.add("mqtt.password", () -> "noaccess");
    }

    @BeforeAll
    static void beforeAll() throws Exception {
        mariadb.start();
        mosquitto.start();

        mqttClient = new MqttClient("tcp://localhost:" + mosquitto.getMappedPort(MQTT_PORT), MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName("johndoe");
        options.setPassword("noaccess".toCharArray());
        mqttClient.connect(options);

        mqttClient.subscribe(CONFIG_TOPIC, (topic, msg) -> {
            DeviceDto dto = objectMapper.readValue(msg.getPayload(), DeviceDto.class);
            assertTrue(receivedMessages.offer(dto));
        });
    }

    @AfterAll
    static void afterAll() throws Exception {
        mqttClient.disconnect();
        mqttClient.close();
        mosquitto.stop();
        mariadb.stop();
    }

    protected DeviceDto createDeviceDto(List<Long> sensorIdList) {
        List<SensorDto> sensorList = new ArrayList<>();
        SensorTypeDto[] types = SensorTypeDto.values();

        for (int i = 0; i < sensorIdList.size(); i++) {
            sensorList.add(SensorDto.builder()
                    .sensorId(sensorIdList.get(i))
                    .sensorType(types[i % types.length]) // wrap around if more IDs than types
                    .build());
        }

        return DeviceDto.builder()
                .macAddress(macAddress)
                .sensors(sensorList)
                .build();
    }

    protected void sendRegister(DeviceDto deviceDto) throws Exception {
        String json = objectMapper.writeValueAsString(deviceDto);
        mqttClient.publish(REGISTER_TOPIC, new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));
    }
}
