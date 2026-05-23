package org.domiot.backend.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.annotation.PreDestroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.domiot.backend.database.SensorValueEntityRepository;
import org.domiot.backend.mapper.SensorValueDtoMapper;
import org.lankheet.domiot.domotics.dto.SensorValueDto;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final Queue<SensorValueEntity> buffer = new ConcurrentLinkedQueue<>();

    /**
     * Save a new sensorValue
     * The sensor is identified by sensorId (after registering the device)
     *
     * @param sensorValueDto The sensorValue to save
     * @return The saved sensorValue
     */
    @Retryable(maxAttempts = 40, backoff = @Backoff(delay = 2000))
    public SensorValueDto saveSensorValue(SensorValueDto sensorValueDto) {
        log.debug("save sensor value {}", sensorValueDto);
        SensorValueEntity sensorValueEntity = sensorValueMapper.map(sensorValueDto);
        SensorValueEntity savedSensorValue = null;
        try {
            savedSensorValue = sensorValueEntityRepository.save(sensorValueEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            buffer.add(sensorValueEntity);
        }
        return sensorValueMapper.map(savedSensorValue);
    }

    @Scheduled(fixedRate = 5000) // Retry every 5 seconds
    public void flushBuffer() {
        while (!buffer.isEmpty()) {
            try {
                sensorValueEntityRepository.save(buffer.poll());
            } catch (Exception e) {
                // If it fails, break and try again in the next flush
                break;
            }
        }
    }

    @PreDestroy
    public void onShutdown() {
        flushBuffer();
    }
}
