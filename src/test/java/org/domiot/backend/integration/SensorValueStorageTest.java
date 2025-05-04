package org.domiot.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.utils.AwaitUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.SensorValueEntity;

@Slf4j
class SensorValueStorageTest extends DomiotBackendTestBase {
    @Test
    void testSensorValueStorage() throws Exception {
        DeviceDto newDevice = createDeviceDto(0L);
        sendRegister(newDevice);

        DeviceDto config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        long sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isEqualTo(1L);

        sendSensorValue(sensorId, LocalDateTime.now(), 0.1);

        SensorValueEntity storedValue = AwaitUtils.awaitOptional(
                () -> sensorValueRepository.findTopBySensorIdOrderByTimeStampDesc(sensorId),
                Duration.ofSeconds(5),
                Duration.ofMillis(200)
        );
        assertThat(storedValue.getValue()).isEqualTo(0.1);
    }

    private void sendSensorValue(long sensorId, LocalDateTime timestamp, double value) throws JsonProcessingException, MqttException {
        SensorValueDto sensorValue = SensorValueDto.builder()
                .sensorId(sensorId)
                .value(value)
                .timeStamp(timestamp)
                .build();
        String json = objectMapper.writeValueAsString(sensorValue);
        mqttClient.publish(SENSOR_VALUE_TOPIC, new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));
    }
}
