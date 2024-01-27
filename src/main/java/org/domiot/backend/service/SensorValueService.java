package org.domiot.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domiot.backend.database.SensorValueEntityRepository;
import org.lankheet.domiot.mapper.SensorValueMapper;
import org.lankheet.domiot.model.SensorValue;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorValueService {
    private final SensorValueEntityRepository sensorValueEntityRepository;
    private final SensorValueMapper sensorValueMapper;

    public SensorValue saveSensorValue(SensorValue sensorValue) {
        // FIXME: Store only new sensors
        // FIXME: sensorvalues and sensor_values (gone after cleanup?)
        return sensorValueMapper.map(sensorValueEntityRepository.save(sensorValueMapper.map(sensorValue)));
    }
}
