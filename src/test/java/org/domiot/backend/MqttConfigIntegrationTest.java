package org.domiot.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.database.SensorEntityRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorDto;
import org.lankheet.domiot.domotics.dto.SensorTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        "logging.config=classpath:logback-spring.xml"
})
@Testcontainers
@EntityScan(basePackages = "org.lankheet.domiot.model")
@Slf4j
class MqttConfigIntegrationTest {
    static ToStringConsumer mosquittoLogConsumer = new ToStringConsumer();
    private static final int MQTT_BROKER_PORT = 1883;
    private static final int MARIA_DB_PORT = 3306;
    private static final String REGISTER_TOPIC = "register";
    public static final String CONFIG_TOPIC = "config";
    private static final String MQTT_CLIENT_ID = "mqttIntegrationTest";
    private static final BlockingQueue<DeviceDto> receivedMessages = new ArrayBlockingQueue<>(1);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static MqttClient mqttClient;

    @Autowired
    private SensorEntityRepository sensorRepository;

    @Autowired
    private DeviceEntityRepository deviceRepository;

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    @Container
    private static final MariaDBContainer<?> MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:10.6")
            .withExposedPorts(MARIA_DB_PORT)
            .withDatabaseName("domiot")
            .withUsername("domiot")
            .withPassword("domiot");

    @Container
    private static final GenericContainer<?> MOSQUITTO_CONTAINER = new GenericContainer<>("eclipse-mosquitto:2.0")
            .withExposedPorts(MQTT_BROKER_PORT)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("mosquitto/mosquitto.conf"),
                    "/mosquitto/config/mosquitto.conf")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("mosquitto/passwords.txt"),
                    "/mosquitto/config/passwords.txt")
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(10)))
            .withLogConsumer(mosquittoLogConsumer)
            .withCreateContainerCmdModifier(cmd -> {
                Objects.requireNonNull(cmd.getHostConfig()).withPortBindings(
                        new com.github.dockerjava.api.model.PortBinding(
                                com.github.dockerjava.api.model.Ports.Binding.bindPort(MQTT_BROKER_PORT),
                                new com.github.dockerjava.api.model.ExposedPort(MQTT_BROKER_PORT)
                        )
                );
            });
    @Autowired
    private SensorEntityRepository sensorEntityRepository;

    @DynamicPropertySource
    static void registerMariadbProperties(DynamicPropertyRegistry registry) {
        log.info("mariadbProperties");
        registry.add("spring.datasource.url", MARIADB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", MARIADB_CONTAINER::getPassword);
        registry.add("spring.datasource.username", MARIADB_CONTAINER::getUsername);

        registry.add("mqtt.clientId", () -> MQTT_CLIENT_ID);
        registry.add("mqtt.url", () -> "tcp://localhost:" + MOSQUITTO_CONTAINER.getFirstMappedPort());
        registry.add("mqtt.username", () -> "johndoe"); // or empty if no auth
        registry.add("mqtt.password", () -> "noaccess"); // or empty if no auth
    }

    @BeforeAll
    public static void startContainers() throws MqttException {
        log.info("start containers");

        MOSQUITTO_CONTAINER.waitingFor(Wait.forHealthcheck());
        // Set up MQTT client
        mqttClient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName("johndoe");
        options.setPassword("noaccess".toCharArray());
        mqttClient.connect(options);
        log.info("client connected");
    }

    @AfterAll
    public static void stopContainers() throws MqttException {
        log.info("stop containers");
        mqttClient.disconnect();
        mqttClient.close();
        MOSQUITTO_CONTAINER.close();
        MARIADB_CONTAINER.close();
    }

    @Test
    void testMqttReceivesRegisterDevice() throws Exception {
        log.info("testMqttReceivesRegisterDevice");
        assertTrue(MOSQUITTO_CONTAINER.isRunning());
        assertTrue(MARIADB_CONTAINER.isRunning());

        String macAddress = UUID.randomUUID().toString();
        DeviceDto deviceDto = DeviceDto.builder()
                .macAddress(macAddress)
                .sensors(List.of(SensorDto.builder()
                        .sensorId(0L)
                        .sensorType(SensorTypeDto.GAS_SENSOR)
                        .build()))
                .build();

        assertTrue(mqttClient.isConnected());

        // Subscribe to "config" topic
        mqttClient.subscribe(CONFIG_TOPIC, (topic, msg) -> {
            log.info("config msg received");
            receivedMessages.offer(objectMapper.readValue(msg.getPayload(), DeviceDto.class));
        });

        String deviceMessage = objectMapper.writeValueAsString(deviceDto);
        // Publish test message
        MqttMessage deviceRegisterMqttMessage = new MqttMessage(deviceMessage.getBytes(StandardCharsets.UTF_8));
        mqttClient.publish(REGISTER_TOPIC, deviceRegisterMqttMessage);

        // Wait for the message to be received
        DeviceDto configuredDevice = receivedMessages.poll(10, TimeUnit.SECONDS);

        // Verify message reception
        assertThat(configuredDevice).isNotNull();
        assertThat(configuredDevice.getMacAddress()).isEqualTo(macAddress);
        assertThat(configuredDevice.getSensors()).hasSize(1);
        assertThat(configuredDevice.getSensors().get(0).getSensorId()).isEqualTo(1L);
        assertThat(configuredDevice.getSensors().get(0).getSensorType()).isEqualTo(SensorTypeDto.GAS_SENSOR);
    }
}
