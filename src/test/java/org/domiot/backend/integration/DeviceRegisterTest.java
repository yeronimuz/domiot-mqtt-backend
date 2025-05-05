package org.domiot.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.lankheet.domiot.domotics.dto.DeviceDto;

/**
 * Unit integration test for device registration
 */
@Slf4j
class DeviceRegisterTest extends DomiotBackendTestBase {

    @Test
    void registerAndReRegisterDeviceShouldStoreAndRespondWithSensorIds() throws Exception {
        DeviceDto newDevice = createDeviceDto(Collections.singletonList(0L));
        sendRegister(newDevice);

        DeviceDto config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        long sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isEqualTo(1L);
        log.info("sensor id after registering: {}", sensorId);

        // Re-register
        DeviceDto existingDevice = createDeviceDto(Collections.singletonList(sensorId));
        sendRegister(existingDevice);

        config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isEqualTo(1L);
        log.info("sensor id after updating: {}", sensorId);
    }

    @Test
    void registerMultipleDevicesShouldStoreAndRespondWithSensorIds() throws Exception {
        DeviceDto device1 = createDeviceDto(Arrays.asList(0L, 0L));
        DeviceDto device2 = createDeviceDto(Arrays.asList(0L, 0L));

        sendRegister(device1);
        DeviceDto config1 = receivedMessages.poll(10, TimeUnit.SECONDS);
        sendRegister(device2);
        DeviceDto config2 = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config1).isNotNull();
        assertThat(config2).isNotNull();
        log.info("config1: {}", config1);
        log.info("config2: {}", config2);

        assertThat(config1.getSensors()).isNotNull();
        assertThat(config1.getSensors()).hasSize(2);
        assertThat(config1.getSensors().get(0).getSensorId()).isEqualTo(1L);
        assertThat(config1.getSensors().get(1).getSensorId()).isEqualTo(2L);

        assertThat(config2.getSensors()).isNotNull();
        assertThat(config2.getSensors()).hasSize(2);
        assertThat(config2.getSensors().get(0).getSensorId()).isEqualTo(3L);
        assertThat(config2.getSensors().get(1).getSensorId()).isEqualTo(4L);
    }
}