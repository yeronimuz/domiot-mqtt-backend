package org.domiot.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.database.SensorEntityRepository;
import org.domiot.backend.database.SensorValueEntityRepository;
import org.domiot.dto.DeviceDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit integration test for device registration
 */
class DeviceRegisterTest extends DomiotBackendTestBase {
    protected static Logger log = LoggerFactory.getLogger(DeviceRegisterTest.class);

    @Autowired
    DeviceRegisterTest(DeviceEntityRepository deviceRepository,
                       SensorEntityRepository sensorRepository,
                       SensorValueEntityRepository sensorValueRepository) {
        super(deviceRepository, sensorRepository, sensorValueRepository);
    }

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
        Thread.sleep(100);
        sendRegister(device2);
        DeviceDto config2 = receivedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(config1).isNotNull();
        assertThat(config2).isNotNull();
        log.info("config1: {}", config1);
        log.info("config2: {}", config2);

        assertThat(config1.getSensors()).isNotNull();
        assertThat(config1.getSensors()).hasSize(2);
        assertThat(config1.getSensors().get(0).getSensorId()).isGreaterThan(0L);
        assertThat(config1.getSensors().get(1).getSensorId()).isGreaterThan(0L);

        assertThat(config2.getSensors()).isNotNull();
        assertThat(config2.getSensors()).hasSize(2);
        assertThat(config2.getSensors().get(0).getSensorId()).isGreaterThan(2L);
        assertThat(config2.getSensors().get(1).getSensorId()).isGreaterThan(3L);
    }
}