package org.domiot.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.database.SensorValueEntityRepository;
import org.domiot.backend.mapper.SensorValueDtoMapper;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
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
    private final SensorValueDtoMapper sensorValueMapper;

    /**
     * Save a new sensorValue
     * The sensor is identified by sensorId (after registering the device)
     *
     * @param sensorValueDto The sensorValue to save
     * @return The saved sensorValue
     */
    public SensorValueDto saveSensorValue(SensorValueDto sensorValueDto) {
        SensorValueEntity sensorValueEntity = sensorValueMapper.map(sensorValueDto);
        SensorValueEntity savedSensorValue = sensorValueEntityRepository.save(sensorValueEntity);
        return sensorValueMapper.map(savedSensorValue);
    }
}
