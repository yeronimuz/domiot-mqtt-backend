package org.domiot.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.lankheet.domiot.domotics.dto.DeviceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit integration test for device registration
 */
class DeviceRegisterTest extends DomiotBackendTestBase {
    protected static Logger log = LoggerFactory.getLogger(DeviceRegisterTest.class);

    @Test
    void registerAndReRegisterDeviceShouldStoreAndRespondWithSensorIds() throws Exception {
        String macAddress = "00:11:22:33:44:55";
        DeviceDto newDevice = createDeviceDto(macAddress, Collections.singletonList(0L));

        sendRegister(newDevice);

        DeviceDto config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        config.getSensors().forEach(sensor -> log.info("Sensor: {}", sensor));
        assertThat(config.getSensors()).hasSize(1);

        long sensorId = config.getSensors().get(0).getSensorId();
        assertThat(sensorId).isGreaterThanOrEqualTo(1L);
        log.info("sensor id after registering: {}", sensorId);

        // Re-register
        DeviceDto existingDevice = createDeviceDto(macAddress, Collections.singletonList(sensorId));
        sendRegister(existingDevice);

        config = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config).isNotNull();
        assertThat(config.getSensors()).isNotNull();
        assertThat(config.getSensors()).hasSize(1);

        long sensorIdAfterResubmit = config.getSensors().get(0).getSensorId();
        assertThat(sensorIdAfterResubmit).isEqualTo(sensorId);
        log.info("sensor id after updating: {}", sensorIdAfterResubmit);
    }

    @Test
    void registerMultipleDevicesShouldStoreAndRespondWithSensorIds() throws Exception {
        assertThat(deviceRepository.count()).isEqualTo(0);

        DeviceDto device1 = createDeviceDto("AA:BB:CC:DD:EE:FF", Arrays.asList(0L, 0L));
        DeviceDto device2 = createDeviceDto("22:33:44:55:66:77", Arrays.asList(0L, 0L));

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
        long device1SensorId1 = config1.getSensors().get(0).getSensorId();
        long device1SensorId2 = config1.getSensors().get(1).getSensorId();
        assertThat(device1SensorId1).isGreaterThan(0L);
        assertThat(device1SensorId2).isGreaterThan(0L);
        assertThat(device1SensorId1).isNotEqualTo(device1SensorId2);

        assertThat(config2.getSensors()).isNotNull();
        assertThat(config2.getSensors()).hasSize(2);
        long device2SensorId1 = config2.getSensors().get(0).getSensorId();
        long device2SensorId2 = config2.getSensors().get(1).getSensorId();
        assertThat(device2SensorId1).isGreaterThan(0L);
        assertThat(device2SensorId2).isGreaterThan(0L);
        assertThat(device2SensorId1).isNotEqualTo(device2SensorId2);

        // Ensure that sensor IDs of device2 do not overlap with those of device1
        assertThat(device2SensorId1).isNotIn(device1SensorId1, device1SensorId2);
        assertThat(device2SensorId2).isNotIn(device1SensorId1, device1SensorId2);
    }
}