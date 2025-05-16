package org.domiot.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        DeviceDto newDevice = createDeviceDto("00:11:22:33:44:55:66", Collections.singletonList(0L));
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

    @Test
    void testMultipleSensorsValueStorage() throws Exception {
        List<Long> sensorIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        DeviceDto newDevice = createDeviceDto("AA:BB:CC:DD:EE:FF", sensorIds);
        sendRegister(newDevice);

        DeviceDto config = receivedMessages.poll(10, TimeUnit.SECONDS);
        assertThat(config).isNotNull();
        log.info(config.toString());

        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(sensorIds.size());

        // sensorValue = 0.1 * sensorId
        sendSensorValue(1L, LocalDateTime.now(), 0.1);
        sendSensorValue(2L, LocalDateTime.now(), 0.2);
        sendSensorValue(3L, LocalDateTime.now(), 0.3);
        sendSensorValue(4L, LocalDateTime.now(), 0.4);
        sendSensorValue(5L, LocalDateTime.now(), 0.5);

        List<SensorValueEntity> storedValues = getSensorValueEntities(sensorIds);
        assertThat(storedValues).hasSize(5);
        for (Long sensorId : sensorIds) {
            storedValues.stream().filter(value -> value.getSensorId().equals(sensorId))
                    .findFirst()
                    .ifPresent(storedValue -> assertThat(storedValue.getValue())
                            .isCloseTo(sensorId * 0.1, within(1e-10)));
        }
    }

    private List<SensorValueEntity> getSensorValueEntities(List<Long> sensorIds) {
        List<SensorValueEntity> list = new ArrayList<>();
        for (Long sensorId : sensorIds) {
            SensorValueEntity sensorValueEntity = AwaitUtils.awaitOptional(
                    () -> sensorValueRepository.findTopBySensorIdOrderByTimeStampDesc(sensorId),
                    Duration.ofSeconds(5),
                    Duration.ofMillis(200)
            );
            list.add(sensorValueEntity);
        }
        return list;
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
