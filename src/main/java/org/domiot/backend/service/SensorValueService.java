package org.domiot.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.database.DeviceEntityRepository;
import org.domiot.backend.database.SensorEntityRepository;
import org.domiot.backend.database.SensorValueEntityRepository;
import org.domiot.backend.mapper.SensorDtoMapper;
import org.domiot.backend.mapper.SensorValueDtoMapper;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.stereotype.Service;

/**
 * SensorValue service layer
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorValueService {
    private final SensorValueEntityRepository sensorValueEntityRepository;
    private final SensorEntityRepository sensorEntityRepository;
    private final DeviceEntityRepository deviceEntityRepository;
    private final SensorValueDtoMapper sensorValueMapper;
    private final SensorDtoMapper sensorMapper;

    public SensorValueDto saveSensorValue(SensorValueDto sensorValueDto) {
        SensorValueEntity sensorValueEntity = sensorValueMapper.map(sensorValueDto);
        sensorValueEntity.getSensorEntity().setName(sensorValueDto.getSensor().getDeviceMac() + '_' + sensorValueDto.getSensor().getSensorType());
        // Need sensorId and deviceId when saving sensorvalue, because sensorvalue.sensorId cannot be null
        enrichSensorValue(sensorValueEntity);
        SensorValueEntity savedSensorValue = sensorValueEntityRepository.save(sensorValueEntity);
        return sensorValueMapper.map(savedSensorValue);
    }

    private void enrichSensorValue(SensorValueEntity sensorValueEntity) {
        DeviceEntity deviceEntity = deviceEntityRepository.findByMacAddress(sensorValueEntity.getSensorEntity().getDeviceEntity().getMacAddress());
        if (deviceEntity == null) {
            deviceEntity = deviceEntityRepository.save(sensorValueEntity.getSensorEntity().getDeviceEntity());
        }

        SensorEntity existingSensoryEntity =
                sensorEntityRepository.findDistinctByNameAndSensorTypeValue(
                        sensorValueEntity.getSensorEntity().getName(),
                        sensorValueEntity.getSensorEntity().getSensorType().getId());
        if (existingSensoryEntity == null) {
            sensorValueEntity.getSensorEntity().setDeviceEntity(deviceEntity);
            existingSensoryEntity = sensorEntityRepository.save(sensorValueEntity.getSensorEntity());
        }

        sensorValueEntity.setSensorEntity(existingSensoryEntity);
    }
}
