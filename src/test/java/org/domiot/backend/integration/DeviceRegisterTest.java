package org.domiot.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;

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
        DeviceDto newDevice = createDeviceDto(0L);
        sendRegister(newDevice);

        DeviceDto config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        long sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isEqualTo(1L);
        log.info("sensor id after registering: {}", sensorId);

        // Re-register
        DeviceDto existingDevice = createDeviceDto(sensorId);
        sendRegister(existingDevice);

        config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isEqualTo(1L);
        log.info("sensor id after updating: {}", sensorId);
    }
}